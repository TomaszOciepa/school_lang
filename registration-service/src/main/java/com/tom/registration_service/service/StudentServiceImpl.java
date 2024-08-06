package com.tom.registration_service.service;

import com.tom.registration_service.model.TokenResponse;
import com.tom.registration_service.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentServiceClient studentServiceClient;
    private final KeycloakServiceClient keycloakServiceClient;

    private final String clientId;
    private final String username;
    private final String password;
    private final String grantType;

    public StudentServiceImpl(StudentServiceClient studentServiceClient, KeycloakServiceClient keycloakServiceClient,
                              @Value("${keycloak.client_id}") String clientId,
                              @Value("${keycloak.username}")String username,
                              @Value("${keycloak.password}") String password,
                              @Value("${keycloak.grant_type}") String grantType) {
        this.studentServiceClient = studentServiceClient;
        this.keycloakServiceClient = keycloakServiceClient;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.grantType = grantType;
    }

    @Override
    public UserDto addStudent(UserDto student) {
        String access_token = getAdminToken().getAccess_token();
        String authorization = "Bearer " + access_token;


        return studentServiceClient.addStudent(authorization, student);
    }

    private TokenResponse getAdminToken() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("client_id", clientId);
        formParams.put("username",username);
        formParams.put("password", password);
        formParams.put("grant_type", grantType);

        return keycloakServiceClient.getAdminToken(formParams);
    }
}
