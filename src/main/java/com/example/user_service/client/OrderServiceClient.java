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
                .uri("/orders/user/{userId}", userId)
                .retrieve()
                .bodyToFlux(Order.class);
    }
}
//@Component
//public class OrderServiceClient {
  //  private final WebClient webClient;

    //public OrderServiceClient() {
      //  this.webClient = WebClient.create("http://ordermicro-env.eba-ha662mef.eu-north-1.elasticbeanstalk.com/");
    //}

    //public Mono<List<Order>> getOrdersByUserId(Long userId) {
      //  return webClient.get()
        //        .uri("/orders/user/{userId}", userId)
          //      .retrieve()
            //    .bodyToMono(new ParameterizedTypeReference<List<Order>>() {});
    //}

//}