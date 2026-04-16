package com.naveen.productservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naveen.productservice.entity.Product;
import com.naveen.productservice.repository.ProductRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
	
	@Autowired
	private ProductRepo repository;
	
	public List<Product> getAllProducts() {
		try {
	        return repository.findAll();
	    } catch (Exception e) {
	        log.error("Error fetching products from DB", e);
	        throw new RuntimeException("Failed to fetch products. Please try again later.");
	    }
	}

	public Optional<Product> getProductById(long productId) {
		try {
	        return repository.findById(productId);
	    } catch (Exception e) {
	        log.error("Error fetching products from DB", e);
	        throw new RuntimeException("Failed to fetch products. Please try again later.");
	    }
	}

	public void saveProduct(Product product) {
		try {
	        repository.save(product);
	    } catch (Exception e) {
	        log.error("Error fetching products from DB", e);
	        throw new RuntimeException("Failed to fetch products. Please try again later.");
	    }
		
	}

}
