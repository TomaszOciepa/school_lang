package com.tom.payment_service.service;


import com.tom.payment_service.model.OrderResponse;
import com.tom.payment_service.model.dto.OrderDto;
import org.springframework.stereotype.Service;


public interface PaymentService {

    OrderResponse createPayment(OrderDto orderDto);
}
