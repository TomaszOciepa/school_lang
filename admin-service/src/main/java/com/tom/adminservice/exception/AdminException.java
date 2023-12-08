package com.tom.adminservice.exception;

public class AdminException extends RuntimeException{

    private AdminError adminError;

    public AdminException(AdminError adminError){
        this.adminError = adminError;
    }

    public AdminError getAdminError() {
        return adminError;
    }
}
