package com.tom.keycloak_client_service.service.student;

import com.tom.keycloak_client_service.exception.KeycloakError;
import com.tom.keycloak_client_service.exception.KeycloakException;
import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.service.keycloak.AdminAuthenticationServiceImpl;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private static Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentServiceClient studentServiceClient;
    private final AdminAuthenticationServiceImpl adminAuthenticationService;

    public StudentServiceImpl(StudentServiceClient studentServiceClient,
                              AdminAuthenticationServiceImpl adminAuthenticationService) {
        this.studentServiceClient = studentServiceClient;
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @Override
    public void createAccount(UserDto student) {
        String accessToken = adminAuthenticationService.getAccessToken();
        try{
            studentServiceClient.addUser(accessToken, student);
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
            if (ex.status() == 409) {
                throw new KeycloakException(KeycloakError.USER_EMAIL_ALREADY_EXISTS);
            }
        }

    }
}
