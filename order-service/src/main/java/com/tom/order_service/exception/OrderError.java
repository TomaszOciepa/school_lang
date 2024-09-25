package com.tom.order_service.exception;

public enum OrderError {

    ORDER_NOT_FOUND("Order does not exists"),

    ORDER_OPERATION_FORBIDDEN("Operation is forbidden."),
    ORDER_STUDENT_ID_CANNOT_BE_EMPTY("Student id cannot be empty"),
    ORDER_TOTAL_AMOUNT_CANNOT_BE_EMPTY("Total amount id cannot be empty"),
    ORDER_COURSE_ID_CANNOT_BE_EMPTY("Course id cannot be empty"),
    STUDENT_OPERATION_FORBIDDEN("Operation is forbidden");

    private String message;

    OrderError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
