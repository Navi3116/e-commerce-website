package com.naveen.productservice.entity.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	
	private Long orderId;
	private long userId;
	private double totalAmount;
	private String status;
	private LocalDateTime orderDate;

}
