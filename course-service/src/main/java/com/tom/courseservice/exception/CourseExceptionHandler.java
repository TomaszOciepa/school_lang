package com.tom.courseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CourseExceptionHandler {

    @ExceptionHandler(CourseException.class)
    public ResponseEntity<ErrorInfo> handleException(CourseException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (CourseError.COURSE_NOT_FOUND.equals(e.getCourseError())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (CourseError.COURSE_NAME_ALREADY_EXISTS.equals(e.getCourseError())
                || CourseError.STUDENT_ALREADY_ENROLLED.equals(e.getCourseError())
                || CourseError.TEACHER_ALREADY_ENROLLED.equals(e.getCourseError())) {
            httpStatus = HttpStatus.CONFLICT;
        } else if (CourseError.COURSE_IS_NOT_ACTIVE.equals(e.getCourseError())
                || CourseError.STUDENT_IS_NOT_ACTIVE.equals(e.getCourseError())
                || CourseError.TEACHER_IS_NOT_ACTIVE.equals(e.getCourseError())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getCourseError().getMessage()));
    }
}
