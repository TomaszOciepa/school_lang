package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final KeycloakService keycloakService;

    @Override
    public UserDto register(UserDto user, String role) {
        keycloakService.createAccount(user, role);
        if( role.equals("student")){
            return studentService.createAccount(user);
        } else if (role.equals("teacher")) {
            return teacherService.createAccount(user);
        }
        return null;
    }
}
