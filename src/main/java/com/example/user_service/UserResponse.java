package com.example.user_service;

import com.example.user_service.dto.Order;

import java.util.List;

public class UserResponse {
    private User user;
    private List<Order> orders;

    public UserResponse(User user, List<Order> orders) {
        this.user = user;
        this.orders = orders;
    }

    // Getters och setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}