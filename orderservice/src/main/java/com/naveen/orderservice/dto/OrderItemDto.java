package com.naveen.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDto {
	
	private Long productId;
	private int quantity;
	private double price;

}
