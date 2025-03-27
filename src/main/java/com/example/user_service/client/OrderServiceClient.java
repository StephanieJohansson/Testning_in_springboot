package com.example.user_service.client;

import com.example.user_service.dto.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class OrderServiceClient {
    private final WebClient webClient;

    public OrderServiceClient(WebClient.Builder webClientBuilder,
                              @Value("${order.service.url}") String orderServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(orderServiceUrl)
                .build();
    }

    public Flux<Order> getOrdersByUserId(Long userId) {
        return webClient.get()
                .uri("/orders/users/{userId}/orders", userId)
                .retrieve()
                .bodyToFlux(Order.class);
    }
}