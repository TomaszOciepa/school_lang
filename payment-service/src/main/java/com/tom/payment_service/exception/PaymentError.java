package com.tom.payment_service.exception;

public enum PaymentError {

    PAYMENT_NOT_FOUND("Payment does not exists"),
    PAYMENT_OPERATION_FORBIDDEN("Operation is forbidden."),
    PAYMENT_STUDENT_ID_CANNOT_BE_EMPTY("Student id cannot be empty"),
    PAYMENT_TOTAL_AMOUNT_CANNOT_BE_EMPTY("Total amount id cannot be empty"),
    PAYMENT_COURSE_ID_CANNOT_BE_EMPTY("Course id cannot be empty");

    private String message;

    PaymentError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
