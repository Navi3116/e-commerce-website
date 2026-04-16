package com.naveen.productservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naveen.productservice.entity.Product;
import com.naveen.productservice.entity.dto.ProductStockInfo;
import com.naveen.productservice.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = service.getAllProducts();

		if (products.isEmpty()) {
			return ResponseEntity.noContent().build(); // 204 No Content
		}
		return ResponseEntity.ok(products); // 200 OK
	}

	@GetMapping("/productbyid")
	public ResponseEntity<Optional<Product>> getProductById(@RequestParam long id) {
		Optional<Product> product = service.getProductById(id);

		if (product == null) {
			return ResponseEntity.noContent().build(); // 204 No Content
		}
		return ResponseEntity.ok(product); // 200 OK
	}

	@PostMapping("/addproduct")
	public ResponseEntity<String> addProduct(@RequestBody Product product) {
		if (product == null) {
			return ResponseEntity.badRequest().body("Product name and price must not be null");
		}
		try {
			service.saveProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving the product");
		}
	}

	@GetMapping("/getstock")
	public ResponseEntity<?> checkStock(@RequestParam long productId, @RequestParam int quantity) {
	    Optional<Product> productOptional = service.getProductById(productId);
	    
	    if (productOptional.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    
	    Product product = productOptional.get();
	    long availableStock = product.getQuantity();

	    if (availableStock >= quantity) {
	        // 1. STOCK SUFFICIENT: Return 200 OK with the ProductStockInfo DTO.
	        ProductStockInfo info = new ProductStockInfo();
	        info.setPrice(product.getPrice()); 
	        info.setAvailableStock(availableStock); 
	        
	        return ResponseEntity.ok(info); // Returns HTTP 200 with the DTO body
	    } else {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                             .body("Insufficient stock. Available: " + availableStock);
	    }
	}
}
