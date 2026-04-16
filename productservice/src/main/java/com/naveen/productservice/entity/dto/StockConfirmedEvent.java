package com.naveen.productservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockConfirmedEvent {
    private Long orderId;
    private String status;
    private String message;
}
