package com.tom.payment_service.controller;

import com.tom.payment_service.model.OrderResponse;
import com.tom.payment_service.model.dto.OrderDto;
import com.tom.payment_service.service.PaymentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {
    private static Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @PostMapping("/create")
    OrderResponse createPayment(@RequestBody OrderDto orderDto){
        logger.info("Post method createPayment(), {}", orderDto.toString());
        return paymentService.createPayment(orderDto);
    }
}
