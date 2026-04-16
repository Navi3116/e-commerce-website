package com.naveen.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessedEvent {

	private Long orderId; // To identify which order this belongs to
	private String paymentId; // The transaction ID from the provider (e.g., Stripe/PayPal)
	private String status; // "SUCCESS", "FAILED", or "DENIED"
	private String reason; // e.g., "Insufficient funds" or "Expired card"

}
