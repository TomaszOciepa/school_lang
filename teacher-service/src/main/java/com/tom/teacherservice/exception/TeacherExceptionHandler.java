package com.tom.teacherservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TeacherExceptionHandler {

    @ExceptionHandler(value = TeacherException.class)
    public ResponseEntity<ErrorInfo> handleException(TeacherException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (TeacherError.TEACHER_NOT_FOUND.equals(e.getTeacherError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (TeacherError.TEACHER_IS_NOT_ACTIVE.equals(e.getTeacherError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (TeacherError.TEACHER_EMAIL_ALREADY_EXISTS.equals(e.getTeacherError())) {
            httpStatus = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getTeacherError().getMessage()));
    }
}

