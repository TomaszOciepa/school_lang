package com.tom.teacherservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "KEYCLOAK-CLIENT-SERVICE")
public interface KeycloakServiceClient {

    @DeleteMapping("/keycloak/delete-account/{email}")
    ResponseEntity<?> deleteAccount(@PathVariable String email);
}
