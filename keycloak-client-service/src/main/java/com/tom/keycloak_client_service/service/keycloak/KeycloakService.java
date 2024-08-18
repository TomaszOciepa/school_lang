package com.tom.keycloak_client_service.service.keycloak;

import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.model.UserKeycloakDto;
import org.springframework.http.ResponseEntity;

public interface KeycloakService {

    UserKeycloakDto createAccount(UserDto user, String role);

    ResponseEntity<Void> deleteAccount(String userEmail);

    ResponseEntity<Void> updateAccount(UserDto updateUserData, String email);

    ResponseEntity<Void> enabledAccount(String email, boolean enabled);
}
