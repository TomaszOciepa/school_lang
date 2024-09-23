package com.tom.order_service.repo;

import com.tom.order_service.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query("{'studentId': ?0}")
    Optional<List<Order>> getOrdersByStudentId(Long studentId);

    @Query("{'orderNumber': ?0}")
    Optional<Order> getByOrderNumber(String orderNumber);

    @Query("{'orderNumber': ?0}")
    boolean existsByOrderNumber(String orderNumber);
}
