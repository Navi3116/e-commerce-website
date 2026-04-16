package com.naveen.orderservice.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequest {
	private Long userId;
    private List<OrderItemDto> items;
}
