package com.naveen.orderservice.dto;

import java.util.List;

import com.naveen.orderservice.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RollbackStockEvent {
    private Long orderId;
    private List<OrderItem> items; // The specific products and quantities
    private String reason;                // Useful for logging (e.g., "PAYMENT_FAILED")
}
