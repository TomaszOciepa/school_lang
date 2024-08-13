package com.tom.keycloak_client_service.service.student;

import com.tom.keycloak_client_service.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @PostMapping("/student")
    UserDto addUser(@RequestHeader("Authorization") String authorization, @RequestBody UserDto user);
}
