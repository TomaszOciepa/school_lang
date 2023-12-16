package com.tom.calendarservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CalendarExceptionHandler {

    @ExceptionHandler(CalendarException.class)
    public ResponseEntity<ErrorInfo> handleException(CalendarException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (CalendarError.CALENDAR_NOT_FOUND.equals(e.getCalendarError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getCalendarError().getMessage()));
    }
}
