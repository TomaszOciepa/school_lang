package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;
import com.tom.registration_service.model.UserKeycloakDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final KeycloakService keycloakService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public UserDto register(UserDto user, String role) {
        UserKeycloakDto userKeycloakDto = keycloakService.createAccount(user, role);
        UserDto account = new UserDto();
        if( role.equals("student")){
          account = studentService.createAccount(user);
        } else if (role.equals("teacher")) {
           account = teacherService.createAccount(user);
        }
        rabbitTemplate.convertAndSend("email-password", userKeycloakDto);
        return account;
    }
}
