package com.tom.order_service.exception;

import feign.FeignException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorInfo> handleException(OrderException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (OrderError.ORDER_NOT_FOUND.equals(e.getOrderError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (OrderError.ORDER_OPERATION_FORBIDDEN.equals(e.getOrderError())) {
            httpStatus = HttpStatus.FORBIDDEN;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getOrderError().getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        return ResponseEntity.status(e.status()).body(new JSONObject(e.contentUTF8()).toMap());
    }
}
