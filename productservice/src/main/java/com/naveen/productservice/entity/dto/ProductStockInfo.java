package com.naveen.productservice.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductStockInfo {

	private Double price;
	private Long availableStock;

}
