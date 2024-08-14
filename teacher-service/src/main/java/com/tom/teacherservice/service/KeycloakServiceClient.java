package com.tom.teacherservice.service;

import com.tom.teacherservice.model.Teacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "KEYCLOAK-CLIENT-SERVICE")
public interface KeycloakServiceClient {

    @DeleteMapping("/keycloak/delete-account/{email}")
    ResponseEntity<?> deleteAccount(@PathVariable String email);

    @PutMapping("/keycloak/update-account/{email}")
    ResponseEntity<?> updateAccount(@RequestBody Teacher teacher, @PathVariable String email);
}
