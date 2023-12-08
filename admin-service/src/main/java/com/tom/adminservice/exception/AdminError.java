package com.tom.adminservice.exception;

public enum AdminError {

    ADMIN_NOT_FOUND("Admin does not exists."),
    ADMIN_IS_NOT_ACTIVE("Admin is not Active."),
    ADMIN_EMAIL_ALREADY_EXISTS("Admin email already exists.");
    private String message;

    AdminError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
