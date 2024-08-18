package com.tom.keycloak_client_service.controller;

import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.service.create_account.CreateAccount;

import com.tom.keycloak_client_service.service.keycloak.KeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/keycloak")
public class KeycloakController {
    private final CreateAccount createAccount;
    private final KeycloakService keycloakService;

//    @CrossOrigin(origins = "http://localhost:9000/registration-service", allowedHeaders = "*", methods = {RequestMethod.POST})
    @PostMapping("/create-account/{role}")
    public ResponseEntity<Void> createAccount(@RequestBody UserDto user,
                                              @PathVariable String role) {
        return createAccount.createAccount(user, role);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/delete-account/{email}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String email) {
        return keycloakService.deleteAccount(email);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @PutMapping("/update-account/{email}")
    public ResponseEntity<Void> updateAccount(@RequestBody UserDto user, @PathVariable String email) {
        return keycloakService.updateAccount(user, email);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PutMapping("/enabled-account/{email}")
    public ResponseEntity<Void> enabledAccount(@PathVariable String email, @RequestParam boolean enabled) {
        return keycloakService.enabledAccount(email, enabled);
    }
}
