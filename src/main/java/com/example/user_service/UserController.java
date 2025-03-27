package com.example.user_service;
import com.example.user_service.client.OrderServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final WebClient webclient;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderServiceClient orderServiceClient;
    private String WEB_CLIENT_URL = System.getenv("WEB_CLIENT_URL");

    @Autowired
    public UserController(WebClient.Builder webclientBuilder,
                          UserRepository userRepository,
                          UserService userService,
                          OrderServiceClient orderServiceClient) {
        this.webclient = webclientBuilder.baseUrl(WEB_CLIENT_URL).build();
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping("/{userId}/orders")
    public Mono<ResponseEntity<UserResponse>> getUserWithOrders(@PathVariable Long userId) {
        return Mono.fromSupplier(() -> userRepository.findById(userId))
                .flatMap(optionalUser -> {
                    if (optionalUser.isEmpty()) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    User user = optionalUser.get();
                    return orderServiceClient.getOrdersByUserId(userId)
                            .collectList()
                            .map(orders -> ResponseEntity.ok(new UserResponse(user, orders)))
                            .onErrorResume(e -> Mono.just(
                                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()));
                });
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
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

    // extra endpoint to get users by email
    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
}