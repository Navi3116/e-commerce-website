package com.naveen.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductStockInfo {

	private Double price;
	private Long availableStock;

}
