package com.example.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("product_name")
    private String product;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("last_updated")
    private String lastUpdated;

    // Default constructor for jackson
    public Order() {
        this.createdAt = Instant.now().toString();
        this.lastUpdated = this.createdAt;
    }

    // full constructor
    public Order(Long id, Long userId, String orderId, String product) {
        this();
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.product = product;
    }

    // constructor for post-requests
    public Order(Long userId, String product) {
        this();
        this.userId = userId;
        this.product = product;
        this.orderId = generateOrderId();
    }

    // Getter/Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Getter/Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    // Getter/Setter
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    // Getter/Setter
    public String getProduct() { return product; }
    public void setProduct(String product) {
        this.product = product;
        this.lastUpdated = Instant.now().toString();
    }

    // Getter for timestamp
    public String getCreatedAt() { return createdAt; }
    public String getLastUpdated() { return lastUpdated; }

    // help method for orderId-generating
    private String generateOrderId() {
        return "ORD-" + Instant.now().toEpochMilli() + "-" + this.userId;
    }
}