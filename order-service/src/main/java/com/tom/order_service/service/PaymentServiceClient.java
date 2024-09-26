package com.tom.order_service.service;

import com.tom.order_service.model.Order;
import com.tom.order_service.model.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentServiceClient {

    @PostMapping("/payment/create")
    OrderResponse createPayment(@RequestBody Order order);
}
