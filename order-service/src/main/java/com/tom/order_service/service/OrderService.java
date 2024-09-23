package com.tom.order_service.service;

import com.tom.order_service.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order getById(String id);

    List<Order> getAllByStudentId(Long id);

    Order getByOrderNumber(String number);

    Order create(Order order);

    void deleteOrderById(String id);

}
