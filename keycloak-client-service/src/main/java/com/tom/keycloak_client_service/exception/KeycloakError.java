package com.tom.keycloak_client_service.exception;

public enum KeycloakError {
    USER_EMAIL_ALREADY_EXISTS("Email already exists."),
    ACCOUNT_CREATION_FAILED("Account creation failed.");
    private String message;

    KeycloakError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
