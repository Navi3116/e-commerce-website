package com.naveen.productservice.entity.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RollbackStockEvent {
	
	private Long orderId;
    private List<OrderItem> items; // The specific products and quantities
    private String reason; 

}
