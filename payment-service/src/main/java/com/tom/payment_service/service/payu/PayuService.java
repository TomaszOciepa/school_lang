package com.tom.payment_service.service.payu;

import com.tom.payment_service.model.Order;
import com.tom.payment_service.model.OrderResponse;
import com.tom.payment_service.model.payu.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PayuService {

    private final WebClient webClient;

    public PayuService(WebClient webClient) {
        this.webClient = webClient;
    }

    public TokenResponse getAuthToken(String clientId, String clientSecret) {
        String url = "https://secure.snd.payu.com/pl/standard/user/oauth/authorize";

        // Wysyłanie żądania POST
        return webClient.post()
                .uri(url)
                .headers(headers -> headers.setBasicAuth(clientId, clientSecret)) // Autoryzacja Basic
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials") // Parametry POST
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();// Zwracanie TokenResponse
    }

    public OrderResponse createOrder(Order order, String accessToken) {
        String url = "https://secure.snd.payu.com/api/v2_1/orders";

        return webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();
    }

    public Order getOrderDetails(String orderId, String accessToken) {
        String url = "https://secure.snd.payu.com/api/v2_1/orders/" + orderId;

        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .retrieve()
                .bodyToMono(Order.class)
                .block();
    }
}


