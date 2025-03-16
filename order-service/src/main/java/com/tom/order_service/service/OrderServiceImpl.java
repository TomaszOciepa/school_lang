package com.tom.order_service.service;

import com.tom.order_service.exception.OrderError;
import com.tom.order_service.exception.OrderException;
import com.tom.order_service.model.Order;
import com.tom.order_service.model.Status;
import com.tom.order_service.model.dto.OrderResponse;
import com.tom.order_service.repo.OrderRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepo;
    private final OrderNumberGenerator orderNumberGenerator;
    private final CourseServiceClient courseServiceClient;
    private final  PaymentServiceClient paymentServiceClient;


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
                .orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
    }

    @Override
    public String create(String courseId, Long studentId) {

        if (studentId == null ){
            throw new OrderException(OrderError.ORDER_STUDENT_ID_CANNOT_BE_EMPTY);
        }

        if(courseId == null){
            throw new OrderException(OrderError.ORDER_COURSE_ID_CANNOT_BE_EMPTY);
        }

        String totalAmount = null;

        try {
           totalAmount = courseServiceClient.getCourseTotalAmount(courseId);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        if(totalAmount == null){
            throw new OrderException(OrderError.ORDER_TOTAL_AMOUNT_CANNOT_BE_EMPTY);
        }

        try {
            courseServiceClient.assignStudentToCourse(courseId, studentId);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        Order order = new Order();
        order.setOrderNumber(orderNumberGenerator.generateUniqueOrderNumber());
        order.setStudentId(studentId);
        order.setCourseId(courseId);
        order.setTotalAmount(totalAmount);
        order.setStatus(Status.WAITING_FOR_PAYMENT);
        order.setCreateDate(LocalDateTime.now());


        String redirectUri = "";
        try {
            OrderResponse orderResponse = paymentServiceClient.createPayment(order);
            order.setPaymentServiceNumber(orderResponse.getOrderId());
            redirectUri = orderResponse.getRedirectUri();
            if(redirectUri == ""){
                orderRepo.save(order);
                throw new OrderException(OrderError.ORDER_URL_IS_EMPTY);
            }
            order.setStatus(Status.COMPLETED);
            orderRepo.save(order);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

       return redirectUri;
    }

    @Override
    public void deleteOrderById(String id) {
        orderRepo.deleteById(id);
    }
}
