package com.tom.payment_service.exception;

public class PaymentException extends RuntimeException{
    private PaymentError paymentError;

    public PaymentException(PaymentError paymentError) {
        this.paymentError = paymentError;
    }

    public PaymentError getPaymentError() {
        return paymentError;
    }
}