package com.example.user_service.dto;


public class Order {

    private Long id;
    private Long userId;
    private String orderId;
    private String product;


    public Order(Long id, Long userId, String orderId, String product) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.product = product;
    }

    public Order() {
    }

    public Long getId(){
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for userId
    public Long getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

