package com.example.user_service;

import com.example.user_service.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(WebClient.Builder webClientBuilder,
                          @Value("${order.service.url}") String orderServiceUrl,
                          UserRepository userRepository,
                          UserService userService) {
        this.webClient = webClientBuilder
                .baseUrl(orderServiceUrl)
                .build();
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("/{userId}/orders")
    public Mono<ResponseEntity<List<Order>>> getUserWithOrders(@PathVariable Long userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/orders")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .bodyToFlux(Order.class)
                .collectList()
                .timeout(Duration.ofSeconds(5))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(Collections.emptyList()));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }
}