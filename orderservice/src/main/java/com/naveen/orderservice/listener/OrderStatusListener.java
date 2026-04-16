package com.naveen.orderservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.naveen.orderservice.dto.OrderReadyForPaymentEvent;
import com.naveen.orderservice.dto.PaymentProcessedEvent;
import com.naveen.orderservice.dto.RollbackStockEvent;
import com.naveen.orderservice.dto.StockConfirmedEvent;
import com.naveen.orderservice.repository.OrderRepo;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderStatusListener {

	private final OrderRepo repository;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public OrderStatusListener(OrderRepo repository, KafkaTemplate<String, Object> kafkaTemplate) {
		this.repository = repository;
		this.kafkaTemplate = kafkaTemplate;
	}

	/**
	 * STEP 1: Listen for Stock Confirmation. Transition: NEW -> INVENTORY_CONFIRMED
	 */

	@KafkaListener(topics = "stock-confirmed", groupId = "order-group")
	@Transactional
	public void handleStockResponse(StockConfirmedEvent response) {
		log.info("Stock status for Order {}: {}", response.getOrderId(), response.getStatus());

		repository.findById(response.getOrderId()).ifPresentOrElse(order -> {
			if ("INVENTORY_CONFIRMED".equalsIgnoreCase(response.getStatus())) {
				order.setStatus("INVENTORY_CONFIRMED");
				repository.save(order);

				// Trigger the Payment Service
				OrderReadyForPaymentEvent paymentRequest = new OrderReadyForPaymentEvent(order.getOrderId(),
						order.getTotalAmount());

				kafkaTemplate.send("order-ready-for-payment", paymentRequest);
				log.info("Inventory confirmed. Payment requested for Order {}", order.getOrderId());
			} else {
				order.setStatus("CANCELLED_OUT_OF_STOCK");
				repository.save(order);
			}
		}, () -> log.error("Order ID {} not found!", response.getOrderId()));
	}

	/**
	 * STEP 2: Listen for Payment Result. Transition: INVENTORY_CONFIRMED ->
	 * COMPLETED
	 */
	@KafkaListener(topics = "payment-result-topic", groupId = "order-group")
	@Transactional
	public void handlePaymentResponse(PaymentProcessedEvent response) {
		log.info("Payment result for Order {}: {}", response.getOrderId(), response.getStatus());

		repository.findById(response.getOrderId()).ifPresentOrElse(order -> {
			if ("FAILED".equalsIgnoreCase(response.getStatus())) {
				order.setStatus("FAILED");

				// 1. Update Order Status
	            order.setStatus("CANCELLED_PAYMENT_FAILED");
	            repository.save(order);

	            // 2. Emit Rollback Event for Product Service
	            // You should include the items/quantities to put back
	            RollbackStockEvent rollback = new RollbackStockEvent(order.getOrderId(), order.getItems(),order.getStatus());
	            kafkaTemplate.send("rollback-stock", rollback);
	            
	            log.warn("Payment failed for Order {}. Triggering stock rollback.", order.getOrderId());
			} else {
				order.setStatus("SUCCESS");
	            log.info("Order {} COMPLETED successfully.", order.getOrderId());
			}
			repository.save(order);
		}, () -> log.error("Order ID {} not found!", response.getOrderId()));
	}

}
