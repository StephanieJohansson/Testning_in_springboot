package com.example.user_service;
import com.example.user_service.client.OrderServiceClient;
import com.example.user_service.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<UserResponse> getUserWithOrders(@PathVariable Long userId) {

        return userRepository.findById(userId)
                .map(user ->
                        orderServiceClient.getOrdersByUserId(userId)  // get orders from Joesfin
                                .collectList()
                                .map(orders -> new UserResponse(user, orders)))
                .orElse(Mono.empty());
    }

    // to connect with Josefins Orders
    //@GetMapping("/{userId}/orders")
    //public List<Order> getUserOrders(@PathVariable Long userId) {
      //  return userService.getUserOrders(userId);
    //}

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

    // extra endpoint to get users by email, for OrderService?
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