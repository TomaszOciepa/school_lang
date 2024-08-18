package com.tom.keycloak_client_service.service.keycloak;

import com.tom.keycloak_client_service.exception.KeycloakError;
import com.tom.keycloak_client_service.exception.KeycloakException;
import com.tom.keycloak_client_service.model.Credential;
import com.tom.keycloak_client_service.model.Role;
import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.model.UserKeycloakDto;
import com.tom.keycloak_client_service.security.AuthenticationContext;
import com.tom.keycloak_client_service.security.JwtUtils;
import com.tom.keycloak_client_service.util.EncryptPassword;
import com.tom.keycloak_client_service.util.PasswordGenerator;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationContext authenticationContext;
    private final JwtUtils jwtUtils;

    public KeycloakServiceImpl(
            KeycloakServiceClient keycloakServiceClient,
            RootAuthenticationServiceImpl rootAuthenticationService, PasswordGenerator passwordGenerator, EncryptPassword encryptPassword, AuthenticationContext authenticationContext, JwtUtils jwtUtils) {
        this.keycloakServiceClient = keycloakServiceClient;
        this.rootAuthenticationService = rootAuthenticationService;
        this.passwordGenerator = passwordGenerator;
        this.encryptPassword = encryptPassword;
        this.authenticationContext = authenticationContext;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public UserKeycloakDto createAccount(UserDto user, String role) {
        String password = passwordGenerator.generatePassword();
        UserKeycloakDto userKeycloakDto = getUserData(user, password);
        String accessToken = rootAuthenticationService.getAccessToken();

        try {
            keycloakServiceClient.createUser(accessToken, userKeycloakDto);
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
            if (ex.status() == 409) {
                throw new KeycloakException(KeycloakError.USER_EMAIL_ALREADY_EXISTS);
            }

        }
        String encryptPass = encryptPassword.encryptPassword(password);
        userKeycloakDto.getCredentials().get(0).setValue(encryptPass);
        assignRole(user, accessToken, role);
        return userKeycloakDto;
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String email) {
        logger.info("Delete Account {}", email);
        String userId = null;
        String accessToken = rootAuthenticationService.getAccessToken();
        try {
            userId = keycloakServiceClient.getUser(accessToken, email).get(0).getId();
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
            if (ex.status() == 404) {
                throw new KeycloakException(KeycloakError.USER_NOT_FOUND);
            }

        }

        keycloakServiceClient.deleteAccount(accessToken, userId);
        logger.info("Account deletion successful for {}", email);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateAccount(UserDto updateUserData, String email) {
        logger.info("Update Account {}", email);
        String accessToken = rootAuthenticationService.getAccessToken();
        List<UserKeycloakDto> userFromKeycloakDb = keycloakServiceClient.getUser(accessToken, email);

        boolean isNotStudent = authenticationContext();

        if(!isNotStudent){
            logger.info("inside isNotStudent");
            String emailFromJwt = jwtUtils.getUserEmailFromJwt();
            if(!isNotStudent && !emailFromJwt.equals(userFromKeycloakDb.get(0).getEmail())){
                throw new KeycloakException(KeycloakError.OPERATION_FORBIDDEN);
            }
        }

        boolean dbUpdateNeeded = false;

        if (updateUserData.getFirstName() != null
                && !updateUserData.getFirstName().equals(userFromKeycloakDb.get(0).getFirstName())) {
            userFromKeycloakDb.get(0).setFirstName(updateUserData.getFirstName());
            dbUpdateNeeded = true;
        }

        if (updateUserData.getLastName() != null
                && !updateUserData.getLastName().equals(userFromKeycloakDb.get(0).getLastName())) {
            userFromKeycloakDb.get(0).setLastName(updateUserData.getLastName());
            dbUpdateNeeded = true;
        }

        if (updateUserData.getEmail() != null
                && !updateUserData.getEmail().equals(userFromKeycloakDb.get(0).getEmail())) {
            userFromKeycloakDb.get(0).setEmail(updateUserData.getEmail());
            userFromKeycloakDb.get(0).setUsername(updateUserData.getEmail());
            dbUpdateNeeded = true;
        }

        if (dbUpdateNeeded) {
            try {
                keycloakServiceClient.updateAccount(
                        accessToken,
                        userFromKeycloakDb.get(0).getId(),
                        userFromKeycloakDb.get(0)
                );
            } catch (FeignException ex) {
                logger.error("FeignException occurred: {}", ex.getMessage());
                if (ex.status() == 409) {
                    throw new KeycloakException(KeycloakError.USER_EMAIL_ALREADY_EXISTS);
                } else {
                    throw new KeycloakException(KeycloakError.ACCOUNT_UPDATE_FAILED);
                }
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public ResponseEntity<Void> enabledAccount(String email, boolean enabled) {
        logger.info("Enabled Account {} set: {}", email, enabled);
        String accessToken = rootAuthenticationService.getAccessToken();

        List<UserKeycloakDto> userFromKeycloakDb = keycloakServiceClient.getUser(accessToken, email);
        userFromKeycloakDb.get(0).setEnabled(enabled);

        try {
            keycloakServiceClient.updateAccount(
                    accessToken,
                    userFromKeycloakDb.get(0).getId(),
                    userFromKeycloakDb.get(0)
            );
            return ResponseEntity.noContent().build();
        } catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
            return ResponseEntity.noContent().build();
        }
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

    private boolean authenticationContext() {
        boolean result = false;
        Authentication authentication = authenticationContext.getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_admin"));
        boolean isTeacher = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_teacher"));
        if (isAdmin || isTeacher) {
            result = true;
        }
        return result;
    }
}
