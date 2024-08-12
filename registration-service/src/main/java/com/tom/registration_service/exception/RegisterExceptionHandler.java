package com.tom.registration_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RegisterExceptionHandler {

    @ExceptionHandler(value = RegisterException.class)
    public ResponseEntity<ErrorInfo> handleException(RegisterException e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if(RegisterError.USER_EMAIL_ALREADY_EXISTS.equals(e.getRegisterError())){
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getRegisterError().getMessage()));
    }
}
