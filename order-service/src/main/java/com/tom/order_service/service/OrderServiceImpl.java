package com.tom.order_service.service;

import com.tom.order_service.exception.OrderError;
import com.tom.order_service.exception.OrderException;
import com.tom.order_service.model.Order;
import com.tom.order_service.model.Status;
import com.tom.order_service.repo.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderNumberGenerator orderNumberGenerator;

    @Override
    public List<Order> getAll() {
        return orderRepo.findAll();
    }

    @Override
    public Order getById(String id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
        return order;
    }

    @Override
    public List<Order> getAllByStudentId(Long id) {
        return orderRepo.getOrdersByStudentId(id)
                .orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
    }

    @Override
    public Order getByOrderNumber(String number) {
       return orderRepo.getByOrderNumber(number)
                .orElseThrow(()-> new OrderException(OrderError.ORDER_NOT_FOUND));
    }

    @Override
    public Order create(Order order) {

        if (order.getStudentId() == null ){
            throw new OrderException(OrderError.ORDER_STUDENT_ID_CANNOT_BE_EMPTY);
        }

        if(order.getCourseId() == null){
            throw new OrderException(OrderError.ORDER_COURSE_ID_CANNOT_BE_EMPTY);
        }

        if(order.getTotalAmount() == null){
            throw new OrderException(OrderError.ORDER_TOTAL_AMOUNT_CANNOT_BE_EMPTY);
        }

        order.setStatus(Status.WAITING_FOR_PAYMENT);
        order.setCreateDate(LocalDateTime.now());
        order.setOrderNumber(orderNumberGenerator.generateUniqueOrderNumber());

        return orderRepo.save(order);
    }

    @Override
    public void deleteOrderById(String id) {
        orderRepo.deleteById(id);
    }
}
