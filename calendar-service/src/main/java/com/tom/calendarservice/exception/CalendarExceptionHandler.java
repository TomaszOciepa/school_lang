package com.tom.calendarservice.exception;

import feign.FeignException;
import org.json.JSONObject;
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
        } else if (CalendarError.CALENDAR_LESSONS_NOT_FOUND.equals(e.getCalendarError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (CalendarError.TEACHER_BUSY_AT_TIME_SLOT.equals(e.getCalendarError())
                || CalendarError.TEACHER_IS_NOT_ENROLLED_IN_COURSE.equals(e.getCalendarError())
                || CalendarError.STUDENT_ALREADY_ENROLLED.equals(e.getCalendarError())
                || CalendarError.LESSON_START_DATE_IS_AFTER_END_DATE.equals(e.getCalendarError())
                || CalendarError.LESSON_START_DATE_IS_AFTER_COURSE_END_DATE.equals(e.getCalendarError())
                || CalendarError.LESSON_START_DATE_IS_BEFORE_COURSE_START_DATE.equals(e.getCalendarError())
                || CalendarError.LESSON_END_DATE_IS_BEFORE_COURSE_START_DATE.equals(e.getCalendarError())
                || CalendarError.LESSON_END_DATE_IS_AFTER_COURSE_END_DATE.equals(e.getCalendarError())
                || CalendarError.LESSON_LIMIT_REACHED_ERROR_MESSAGE.equals(e.getCalendarError())
        ) {
            httpStatus = HttpStatus.CONFLICT;
        } else if (CalendarError.STUDENT_OPERATION_FORBIDDEN.equals(e.getCalendarError())) {
            httpStatus = HttpStatus.FORBIDDEN;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getCalendarError().getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        return ResponseEntity.status(e.status()).body(new JSONObject(e.contentUTF8()).toMap());
    }
}
