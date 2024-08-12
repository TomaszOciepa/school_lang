package com.tom.registration_service.service;

import com.tom.registration_service.exception.RegisterError;
import com.tom.registration_service.exception.RegisterException;
import com.tom.registration_service.model.*;
import com.tom.registration_service.util.EncryptPassword;
import com.tom.registration_service.util.PasswordGenerator;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private static Logger logger = LoggerFactory.getLogger(KeycloakServiceImpl.class);
    private final KeycloakServiceClient keycloakServiceClient;
    private final RootAuthenticationServiceImpl rootAuthenticationService;
    private final PasswordGenerator passwordGenerator;
    private final EncryptPassword encryptPassword;

    public KeycloakServiceImpl(
            KeycloakServiceClient keycloakServiceClient,
            RootAuthenticationServiceImpl rootAuthenticationService, PasswordGenerator passwordGenerator, EncryptPassword encryptPassword) {
        this.keycloakServiceClient = keycloakServiceClient;
        this.rootAuthenticationService = rootAuthenticationService;
        this.passwordGenerator = passwordGenerator;
        this.encryptPassword = encryptPassword;
    }


    @Override
    public UserKeycloakDto createAccount(UserDto user, String role) {
        String password = passwordGenerator.generatePassword();
        UserKeycloakDto userKeycloakDto = getUserData(user,password);
        String accessToken = rootAuthenticationService.getAccessToken();


        try {
            keycloakServiceClient.createUser(accessToken, userKeycloakDto);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
            if(ex.status() == 409){
                throw new RegisterException(RegisterError.USER_EMAIL_ALREADY_EXISTS);
            }

        }
        String encryptPass = encryptPassword.encryptPassword(password);
        userKeycloakDto.getCredentials().get(0).setValue(encryptPass);
        assignRole(user, accessToken, role);
        return userKeycloakDto;
    }

    private void assignRole(UserDto user, String authorization, String role) {
        String newUserId = keycloakServiceClient.getUser(authorization, user.getEmail()).get(0).getId();
        Role roleStudent = keycloakServiceClient.getRole(authorization, role);
        List<Role> roles = new ArrayList<>();
        roles.add(roleStudent);
        keycloakServiceClient.assignRole(authorization, newUserId, roles);
    }

    private UserKeycloakDto getUserData(UserDto user, String password) {
        List<Credential> credentials = new ArrayList<>();
        credentials.add(new Credential("password", password, true));
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
