package com.tom.registration_service.service;

import com.tom.registration_service.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakServiceClient keycloakServiceClient;
    private final RootAuthenticationServiceImpl rootAuthenticationService;

    public KeycloakServiceImpl(
            KeycloakServiceClient keycloakServiceClient,
            RootAuthenticationServiceImpl rootAuthenticationService) {
        this.keycloakServiceClient = keycloakServiceClient;
        this.rootAuthenticationService = rootAuthenticationService;
    }


    @Override
    public void createAccount(UserDto user, String role) {
        UserKeycloakDto userKeycloakDto = getUserData(user);
        String accessToken = rootAuthenticationService.getAccessToken();
        keycloakServiceClient.createUser(accessToken, userKeycloakDto);
        assignRole(user, accessToken, role);
    }

    private void assignRole(UserDto user, String authorization, String role) {
        String newUserId = keycloakServiceClient.getUser(authorization, user.getEmail()).get(0).getId();
        Role roleStudent = keycloakServiceClient.getRole(authorization, role);
        List<Role> roles = new ArrayList<>();
        roles.add(roleStudent);
        keycloakServiceClient.assignRole(authorization, newUserId, roles);
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
