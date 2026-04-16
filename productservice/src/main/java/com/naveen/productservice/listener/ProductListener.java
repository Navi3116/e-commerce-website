package com.naveen.productservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.naveen.productservice.entity.Product;
import com.naveen.productservice.entity.dto.OrderItem;
import com.naveen.productservice.entity.dto.OrderPlacedEvent;
import com.naveen.productservice.entity.dto.RollbackStockEvent;
import com.naveen.productservice.entity.dto.StockConfirmedEvent;
import com.naveen.productservice.repository.ProductRepo;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductListener {

	private final ProductRepo repository;
	// Change String to Object or StockConfirmedEvent to allow JSON serialization
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public ProductListener(ProductRepo repository, KafkaTemplate<String, Object> kafkaTemplate) {
		this.repository = repository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@KafkaListener(topics = "order-placed", groupId = "product-group")
	@Transactional
	public void handleOrderPlaced(OrderPlacedEvent event) {
		log.info("Received order event for ID: {}", event.getOrderId());
		boolean allItemsInStock = true;

		for (var item : event.getItems()) {
			Product product = repository.findById(item.getProductId()).orElse(null);

			if (product == null || product.getQuantity() < item.getQuantity()) {
				log.warn("Stock insufficient for Product ID: {}", item.getProductId());
				allItemsInStock = false;
				break;
			}
		}

		if (allItemsInStock) {
			// 1. Deduct Stock
			deductStock(event);
			// 2. Send SUCCESS
			kafkaTemplate.send("stock-confirmed",
					new StockConfirmedEvent(event.getOrderId(), "INVENTORY_CONFIRMED", "Inventory reserved"));
			log.info("Stock confirmed and deducted for Order ID: {}", event.getOrderId());
		} else {
			// 2. Send FAILED
			kafkaTemplate.send("stock-confirmed",
					new StockConfirmedEvent(event.getOrderId(), "FAILED", "Insufficient stock available"));
		}
	}

	@KafkaListener(topics = "rollback-stock", groupId = "product-group")
	@Transactional
	public void handleStockRollback(RollbackStockEvent event) {
		log.info("Rolling back stock for Order {}", event.getOrderId());

		for (OrderItem item : event.getItems()) {
			repository.findById(item.getProductId()).ifPresent(product -> {
				// Increment the quantity back
				product.setQuantity(product.getQuantity() + item.getQuantity());
				repository.save(product);
			});
		}
	}

	private void deductStock(OrderPlacedEvent event) {
		event.getItems().forEach(item -> {
			Product product = repository.findById(item.getProductId()).get();
			product.setQuantity(product.getQuantity() - item.getQuantity());
			repository.save(product);
		});
	}
}
