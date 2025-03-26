package com.example.user_service.client;

import com.example.user_service.dto.Order;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class OrderServiceClient {
    private final WebClient webClient;

    public OrderServiceClient() {
        this.webClient = WebClient.create("http://order-service-env.abc123.eu-north-1.elasticbeanstalk.com"); // put Josefins URL later
    }

    public Mono<List<Order>> getOrdersByUserId(Long userId) {
        return webClient.get()
                .uri("/orders/user/{userId}", userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Order>>() {});
    }
}