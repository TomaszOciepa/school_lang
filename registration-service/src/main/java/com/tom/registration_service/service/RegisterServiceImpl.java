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
    public UserDto register(UserDto user, String role) {
        keycloakService.createUser(user, role);
        if( role.equals("student")){
            return studentService.addStudent(user);
        } else if (role.equals("teacher")) {
            return null;
        }
        return null;
    }
}
