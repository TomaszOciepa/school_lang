package com.tom.keycloak_client_service.service.teacher;

import com.tom.keycloak_client_service.exception.KeycloakError;
import com.tom.keycloak_client_service.exception.KeycloakException;
import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.service.keycloak.AdminAuthenticationServiceImpl;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherServiceClient teacherServiceClient;
    private final AdminAuthenticationServiceImpl adminAuthenticationService;

    public TeacherServiceImpl(TeacherServiceClient teacherServiceClient, AdminAuthenticationServiceImpl adminAuthenticationService) {
        this.teacherServiceClient = teacherServiceClient;
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @Override
    public void createAccount(UserDto userDto) {
        String accessToken = adminAuthenticationService.getAccessToken();

         try {
             teacherServiceClient.addUser(accessToken, userDto);
         }catch (FeignException ex) {
             logger.error("FeignException occurred: {}", ex.getMessage());
             if (ex.status() == 409) {
                 throw new KeycloakException(KeycloakError.USER_EMAIL_ALREADY_EXISTS);
             }
         }
    }
}
