package com.tom.payment_service.exception;

import feign.FeignException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorInfo> handleException(PaymentException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (PaymentError.PAYMENT_NOT_FOUND.equals(e.getPaymentError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (PaymentError.PAYMENT_OPERATION_FORBIDDEN.equals(e.getPaymentError())) {
            httpStatus = HttpStatus.FORBIDDEN;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getPaymentError().getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        return ResponseEntity.status(e.status()).body(new JSONObject(e.contentUTF8()).toMap());
    }
}