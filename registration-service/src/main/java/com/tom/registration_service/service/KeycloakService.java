package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;
import com.tom.registration_service.model.UserKeycloakDto;

public interface KeycloakService {

    UserKeycloakDto createAccount(UserDto user, String role);

}
