package com.devops.shop;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "orders")
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long productId;
    private int quantity;
    private String status = "CREATED";
    private LocalDateTime createdAt = LocalDateTime.now();
    protected OrderEntity() {}
    public OrderEntity(Long productId, int quantity) { this.productId = productId; this.quantity = quantity; }
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
