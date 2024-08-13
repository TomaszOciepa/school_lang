package com.tom.keycloak_client_service.service.keycloak;

import com.tom.keycloak_client_service.model.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RootAuthenticationServiceImpl implements AuthenticationService {

    private final String clientId;
    private final String username;
    private final String password;
    private final String grantType;
    private final KeycloakServiceClient keycloakServiceClient;

    public RootAuthenticationServiceImpl(
            @Value("${keycloak.root.client_id}") String clientId,
            @Value("${keycloak.root.username}") String username,
            @Value("${keycloak.root.password}") String password,
            @Value("${keycloak.root.grant_type}") String grantType,
            KeycloakServiceClient keycloakServiceClient) {
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.grantType = grantType;
        this.keycloakServiceClient = keycloakServiceClient;
    }

    @Override
    public String getAccessToken() {
        String access_token = getRootToken().getAccess_token();
        String authorization = "Bearer " + access_token;
        return authorization;
    }


    private TokenResponse getRootToken() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("client_id", clientId);
        formParams.put("username", username);
        formParams.put("password", password);
        formParams.put("grant_type", grantType);

        return keycloakServiceClient.getRootToken(formParams);
    }

}
