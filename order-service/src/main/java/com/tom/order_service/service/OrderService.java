package com.tom.order_service.service;

import com.tom.order_service.model.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getById(String id);

    List<Order> getAllByStudentId(Long id);

    Order getByOrderNumber(String number);

    ResponseEntity<Void> create(String courseId, Long studentId);

    void deleteOrderById(String id);

}
