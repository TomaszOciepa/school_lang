package com.tom.order_service.exception;


public class OrderException extends RuntimeException{

    private OrderError orderError;

    public OrderException(OrderError orderError) {
        this.orderError = orderError;
    }

    public OrderError getOrderError() {
        return orderError;
    }
}
