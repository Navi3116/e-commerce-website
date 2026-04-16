package com.naveen.productservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	
	private Long OrderItemId;
    private Order order;
	private long productId;
	private int quantity;
	private double price;

}
