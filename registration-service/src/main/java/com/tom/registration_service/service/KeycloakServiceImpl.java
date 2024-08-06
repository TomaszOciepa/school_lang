package com.tom.registration_service.service;

import com.tom.registration_service.model.Credential;
import com.tom.registration_service.model.TokenResponse;
import com.tom.registration_service.model.UserDto;
import com.tom.registration_service.model.UserKeycloakDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final String clientId;
    private final String username;
    private final String password;
    private final String grantType;
    private final KeycloakServiceClient keycloakServiceClient;

    public KeycloakServiceImpl(
            @Value("${keycloak.admin.client_id}") String clientId,
            @Value("${keycloak.admin.username}") String username,
            @Value("${keycloak.admin.password}") String password,
            @Value("${keycloak.admin.grant_type}") String grantType,
            KeycloakServiceClient keycloakServiceClient) {
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.grantType = grantType;
        this.keycloakServiceClient = keycloakServiceClient;
    }

    @Override
    public void createUser(UserDto user) {
        String access_token = getRootToken().getAccess_token();
        String authorization = "Bearer " + access_token;

        UserKeycloakDto userKeycloakDto = getUserData(user);
        keycloakServiceClient.createUser(authorization, userKeycloakDto);
    }

    private TokenResponse getRootToken() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("client_id", clientId);
        formParams.put("username", username);
        formParams.put("password", password);
        formParams.put("grant_type", grantType);

        return keycloakServiceClient.getRootToken(formParams);
    }
    private UserKeycloakDto getUserData(UserDto user) {
        List<Credential> credentials = new ArrayList<>();
        credentials.add(new Credential("password", user.getPassword(), false));
        UserKeycloakDto userKeycloakDto = new UserKeycloakDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                true,
                true,
                credentials
        );
        return userKeycloakDto;
    }
}
