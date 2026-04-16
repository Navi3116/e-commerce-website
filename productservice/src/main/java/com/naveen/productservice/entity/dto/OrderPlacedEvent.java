package com.naveen.productservice.entity.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
	private Long orderId;
	private Long userId;
	private List<EventItem> items;
	
}
