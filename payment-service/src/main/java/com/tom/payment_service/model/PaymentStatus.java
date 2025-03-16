package com.tom.payment_service.model;

public enum PaymentStatus {
    WAITING_FOR_PAYMENT,
    PENDING,
    WAITING_FOR_CONFIRMATION,
    COMPLETED,
    CANCELED,
}
