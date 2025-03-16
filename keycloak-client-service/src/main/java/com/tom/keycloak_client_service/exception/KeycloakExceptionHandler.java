package com.tom.keycloak_client_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KeycloakExceptionHandler {

    @ExceptionHandler(value = KeycloakException.class)
    public ResponseEntity<ErrorInfo> handleException(KeycloakException e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if(KeycloakError.USER_EMAIL_ALREADY_EXISTS.equals(e.getKeycloakError())){
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getKeycloakError().getMessage()));
    }
}
