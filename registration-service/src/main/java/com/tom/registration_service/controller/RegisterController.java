package com.tom.registration_service.controller;

import com.tom.registration_service.model.UserDto;
import com.tom.registration_service.service.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping
    public UserDto addStudent(@RequestBody UserDto student) {
        return registerService.register(student);
    }

}
