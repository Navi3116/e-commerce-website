package com.naveen.orderservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
//@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	@Column(name = "user_id")
	private long userId;
	@Column(name = "total_amount")
	private double totalAmount;
	@Column(name = "status")
	private String status;
	
	 @Column(name = "order_date")
	 private LocalDateTime orderDate;

	 // 3. Relationship (One-to-Many with OrderItem)
	 @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 private List<OrderItem> items; 
	    
	 public Order() {
	     this.orderDate = LocalDateTime.now(); // Set creation date automatically
	     this.status = "PENDING"; // Default status for a new order
	 }

}
