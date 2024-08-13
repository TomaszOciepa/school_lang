package com.tom.keycloak_client_service.service.student;

import com.tom.keycloak_client_service.model.UserDto;

public interface StudentService {

    void createAccount(UserDto user);
}
