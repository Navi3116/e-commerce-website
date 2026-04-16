package com.naveen.orderservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.naveen.orderservice.dto.EventItem;
import com.naveen.orderservice.dto.OrderPlacedEvent;
import com.naveen.orderservice.dto.OrderRequest;
import com.naveen.orderservice.entity.Order;
import com.naveen.orderservice.entity.OrderItem;
import com.naveen.orderservice.repository.OrderRepo;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private final OrderRepo repository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderService(OrderRepo repository, KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Long placeOrder(OrderRequest request) {
        log.info("Starting asynchronous order placement for user: {}", request.getUserId());

        // 1. Create Order in PENDING state
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = request.getItems().stream().map(itemDto -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemDto.getProductId());
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(itemDto.getPrice()); // Assume price is sent or fetched from local cache
            return item;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        double totalAmount = orderItems.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        order.setTotalAmount(totalAmount);

        // 2. Save locally first (Transactional Outbox pattern start)
        Order savedOrder = repository.save(order);

        // 3. Map to Event
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderId(savedOrder.getOrderId());
        event.setUserId(savedOrder.getUserId());
        event.setItems(savedOrder.getItems().stream()
            .map(i -> new EventItem(i.getProductId(), i.getQuantity(), i.getPrice()))
            .collect(Collectors.toList()));

        // 4. Publish to Kafka (Fire and Forget)
        kafkaTemplate.send("order-placed", String.valueOf(savedOrder.getOrderId()), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("OrderCreatedEvent sent to Kafka for Order ID: {}", savedOrder.getOrderId());
                } else {
                    log.error("Kafka failure for Order ID: {}", savedOrder.getOrderId(), ex);
                }
            });

        return savedOrder.getOrderId();
    }

    public Optional<Order> getOrderById(Long orderId) {
        log.info("Fetching details for Order ID: {}", orderId);
        try {
            return repository.findById(orderId);
        } catch (Exception e) {
            log.error("Database error while fetching Order ID: {}. Error: {}", orderId, e.getMessage());
            return Optional.empty(); 
        }
    }

}