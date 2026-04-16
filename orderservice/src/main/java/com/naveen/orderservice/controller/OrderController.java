package com.naveen.orderservice.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naveen.orderservice.dto.OrderRequest;
import com.naveen.orderservice.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

	@Autowired
	private OrderService service;

	@PostMapping("/placeorder")
	public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
	    try {
	        log.info("Order request received for user: {}", request.getUserId());
	        long orderId = service.placeOrder(request);
	        
	        // Return 202 Accepted instead of 200 OK
	        return ResponseEntity.accepted()
	            .body("Order " + orderId + " is now PENDING. We are validating your stock...");
	    } catch (Exception e) {
	        log.error("Error initiating order: ", e);
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
	            .body("Could not initiate order. Please try again.");
	    }
	}
	
	@GetMapping("/status")
	public ResponseEntity<String> getOrderStatus(@RequestParam Long orderId) {
	    return service.getOrderById(orderId)
	        .map(order -> ResponseEntity.ok("Order Status: " + order.getStatus()))
	        .orElse(ResponseEntity.notFound().build());
	}

}
