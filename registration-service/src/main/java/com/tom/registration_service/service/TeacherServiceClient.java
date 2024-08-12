package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "TEACHER-SERVICE")
public interface TeacherServiceClient {

    @PostMapping("/teacher")
    UserDto addUser(@RequestHeader("Authorization") String authorization, @RequestBody UserDto user);
}