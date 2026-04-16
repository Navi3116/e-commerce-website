package com.naveen.productservice.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

	@Id
	@Nonnull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "product_name")
	private String name;
	@Column(name = "product_price")
	private double price;
	@Column(name = "product_desc")
	private String desc;
	@Column(name = "product_quantity")
	private long quantity;
}
