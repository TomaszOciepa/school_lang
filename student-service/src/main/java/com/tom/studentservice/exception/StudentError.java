package com.tom.studentservice.exception;

public enum StudentError {

    STUDENT_NOT_FOUND("Student does not exists."),
    STUDENT_IS_NOT_ACTIVE("Student is not Active.");

    private String message;

    StudentError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
