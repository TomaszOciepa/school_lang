package com.tom.keycloak_client_service.service.create_account;

import com.tom.keycloak_client_service.model.UserDto;
import org.springframework.http.ResponseEntity;

public interface CreateAccount {

    ResponseEntity<Void> createAccount(UserDto user, String role);
}
