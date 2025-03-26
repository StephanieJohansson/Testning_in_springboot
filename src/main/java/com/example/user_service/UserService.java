package com.example.user_service;

import com.example.user_service.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.user_service.client.OrderServiceClient;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderServiceClient orderServiceClient;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Extra method to find user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Order> getUserOrders(Long userId) {
        return List.of();
    }
}