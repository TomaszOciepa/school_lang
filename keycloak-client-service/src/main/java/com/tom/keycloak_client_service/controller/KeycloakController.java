package com.tom.keycloak_client_service.controller;

import com.tom.keycloak_client_service.model.UserDto;
import com.tom.keycloak_client_service.service.create_account.CreateAccount;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/keycloak")
public class KeycloakController {
    private final CreateAccount createAccount;

//    @CrossOrigin(origins = "http://localhost:9000/registration-service", allowedHeaders = "*", methods = {RequestMethod.POST})
    @PostMapping("/create-account/{role}")
    public ResponseEntity<Void> createAccount(@RequestBody UserDto user,
                                              @PathVariable String role) {
        return createAccount.createAccount(user, role);
    }
}
