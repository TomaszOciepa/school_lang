package com.tom.keycloak_client_service.service.teacher;

import com.tom.keycloak_client_service.model.UserDto;

public interface TeacherService {
    void createAccount(UserDto userDto);
}