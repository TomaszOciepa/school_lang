package com.tom.keycloak_client_service.controller;

import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.service.create_account.CreateAccount;

import com.tom.keycloak_client_service.service.keycloak.KeycloakService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/keycloak")
public class KeycloakController {
    private final CreateAccount createAccount;
    private final KeycloakService keycloakService;
    private static Logger logger = LoggerFactory.getLogger(KeycloakController.class);

    @PostMapping("/create-account/{role}")
    public ResponseEntity<Void> createAccount(@RequestBody UserDto user,
                                              @PathVariable String role) {
        logger.info("Post method createAccount().");
        return createAccount.createAccount(user, role);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/delete-account/{email}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String email) {
        logger.info("Delete method deleteAccount().");
        return keycloakService.deleteAccount(email);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @PutMapping("/update-account/{email}")
    public ResponseEntity<Void> updateAccount(@RequestBody UserDto user, @PathVariable String email) {
        logger.info("Put method updateAccount().");
        return keycloakService.updateAccount(user, email);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PutMapping("/enabled-account/{email}")
    public ResponseEntity<Void> enabledAccount(@PathVariable String email, @RequestParam boolean enabled) {
        logger.info("Put method enabledAccount().");
        return keycloakService.enabledAccount(email, enabled);
    }
}
