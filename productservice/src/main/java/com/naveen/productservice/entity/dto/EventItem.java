package com.naveen.productservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventItem {
	private Long productId;
	private int quantity;
	private Double price;
}