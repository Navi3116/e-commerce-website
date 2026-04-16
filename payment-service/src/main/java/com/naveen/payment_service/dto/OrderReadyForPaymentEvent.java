package com.naveen.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadyForPaymentEvent {
	private Long orderId;
	private double totalAmount;
	private String customerEmail;
}
