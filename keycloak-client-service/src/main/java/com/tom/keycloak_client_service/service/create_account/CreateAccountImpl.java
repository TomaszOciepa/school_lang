package com.tom.keycloak_client_service.service.create_account;

import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.model.UserKeycloakDto;
import com.tom.keycloak_client_service.service.keycloak.KeycloakService;
import com.tom.keycloak_client_service.service.student.StudentService;
import com.tom.keycloak_client_service.service.teacher.TeacherService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateAccountImpl implements CreateAccount {

    private final KeycloakService keycloakService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RabbitTemplate rabbitTemplate;

    public CreateAccountImpl(KeycloakService keycloakService, StudentService studentService, TeacherService teacherService, RabbitTemplate rabbitTemplate) {
        this.keycloakService = keycloakService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ResponseEntity<Void> createAccount(UserDto user, String role) {

        UserKeycloakDto userKeycloakDto = keycloakService.createAccount(user, role);

        if (role.equals("student")) {
            studentService.createAccount(user);
        } else if (role.equals("teacher")) {
            teacherService.createAccount(user);
        }

        rabbitTemplate.convertAndSend("email-password", userKeycloakDto);
        return ResponseEntity.ok().build();
    }
}
