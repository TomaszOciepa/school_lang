package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @PostMapping("/student")
    UserDto addStudent(@RequestHeader("Authorization") String authorization, @RequestBody UserDto student);
}
