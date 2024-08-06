package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final StudentService studentService;
    private final KeycloakService keycloakService;

    @Override
    public UserDto register(UserDto user) {
        keycloakService.createUser(user);

        return studentService.addStudent(user);

    }
}
