package com.example.user_service;

//import com.example.user_service.model.User;
import com.example.user_service.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Method to find user by email
    User findByEmail(String email);
}