package com.tom.registration_service.exception;

public class RegisterException extends RuntimeException{

    private RegisterError registerError;

    public RegisterException(RegisterError registerError) {
        this.registerError = registerError;
    }

    public RegisterError getRegisterError() {
        return registerError;
    }
}
