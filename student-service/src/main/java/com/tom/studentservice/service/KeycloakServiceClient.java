package com.tom.studentservice.service;

import com.tom.studentservice.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "KEYCLOAK-CLIENT-SERVICE")
public interface KeycloakServiceClient {

    @DeleteMapping("/keycloak/delete-account/{email}")
    ResponseEntity<?> deleteAccount(@PathVariable String email);
    @PutMapping("/keycloak/update-account/{email}")
    ResponseEntity<?> updateAccount(@RequestBody Student student, @PathVariable String email);

    @PutMapping("/keycloak/enabled-account/{email}")
    ResponseEntity<Void> enabledAccount(@PathVariable String email, @RequestParam boolean enabled);
}
