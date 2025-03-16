package com.tom.order_service.model;

public enum Status {
    NEW,
    WAITING_FOR_PAYMENT,
    PENDING,
    WAITING_FOR_CONFIRMATION,
    COMPLETED,
    CANCELED,
}

