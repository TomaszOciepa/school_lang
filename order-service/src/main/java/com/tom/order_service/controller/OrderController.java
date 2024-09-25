package com.tom.order_service.controller;

import com.tom.order_service.model.Order;
import com.tom.order_service.service.OrderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public List<Order> getAllOrders() {
        logger.info("Get method getAllOrders()");
        return orderService.getAll();
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @GetMapping("/{id}")
    public Order getById(@PathVariable String id) {
        logger.info("Get method getById()");
        return orderService.getById(id);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @GetMapping("/student/{studentId}")
    public List<Order> getOrdersByStudentId(@PathVariable Long studentId) {
        logger.info("Get method getOrdersByStudentId() studentId: {}", studentId);
        return orderService.getAllByStudentId(studentId);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @GetMapping("/number/{number}")
    public Order getOrderByOrderNumber(@PathVariable String number) {
        logger.info("Get method getOrderByOrderNumber() number: {}", number);
        return orderService.getByOrderNumber(number);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @PostMapping("/course/{courseId}/student/{studentId}")
    public ResponseEntity<Void> createOrder(@PathVariable String courseId, @PathVariable Long studentId) {
        logger.info("Post method createOrder().");
        return orderService.create(courseId, studentId);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/delete/{id}")
    public void deleteOrderById(@PathVariable String id) {
        logger.info("Delete method deleteOrderById().");
        orderService.deleteOrderById(id);
    }

}
