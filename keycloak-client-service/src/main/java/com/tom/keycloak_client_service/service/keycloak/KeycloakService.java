package com.tom.keycloak_client_service.service.keycloak;

import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.model.UserKeycloakDto;

public interface KeycloakService {

    UserKeycloakDto createAccount(UserDto user, String role);
}
