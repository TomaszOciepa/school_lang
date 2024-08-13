package com.tom.keycloak_client_service.exception;

public class KeycloakException extends RuntimeException{

    private KeycloakError keycloakError;

    public KeycloakException(KeycloakError keycloakError) {
        this.keycloakError = keycloakError;
    }

    public KeycloakError getKeycloakError() {
        return keycloakError;
    }
}
