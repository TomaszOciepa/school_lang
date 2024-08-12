package com.tom.registration_service.exception;

public enum RegisterError {

    USER_EMAIL_ALREADY_EXISTS("Email already exists.");
    private String message;

    RegisterError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
