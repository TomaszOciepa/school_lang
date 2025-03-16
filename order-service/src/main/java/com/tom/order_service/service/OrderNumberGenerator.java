package com.tom.order_service.service;

import com.tom.order_service.repo.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@AllArgsConstructor
@Component
public class OrderNumberGenerator {

    private final OrderRepository orderRepo;

    public String generateUniqueOrderNumber() {
        String orderNumber;
        do {
            orderNumber = generateOrderNumber();
        } while (orderRepo.existsByOrderNumber(orderNumber));

        return orderNumber;
    }

    private String generateOrderNumber() {

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePart = dateFormat.format(now);


        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000);

        return datePart + "-" + randomNumber;
    }
}

