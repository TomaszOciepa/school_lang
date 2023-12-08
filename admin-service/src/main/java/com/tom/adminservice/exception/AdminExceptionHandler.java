package com.tom.adminservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(value = AdminException.class)
    public ResponseEntity<ErrorInfo> handleException(AdminException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (AdminError.ADMIN_NOT_FOUND.equals(e.getAdminError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (AdminError.ADMIN_IS_NOT_ACTIVE.equals(e.getAdminError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (AdminError.ADMIN_EMAIL_ALREADY_EXISTS.equals(e.getAdminError())) {
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getAdminError().getMessage()));
    }
}
