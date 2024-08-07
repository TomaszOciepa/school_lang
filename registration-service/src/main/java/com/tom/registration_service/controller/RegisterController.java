package com.tom.registration_service.controller;

import com.tom.registration_service.model.UserDto;
import com.tom.registration_service.service.RegisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/{role}")
    public UserDto createAccount(@RequestBody UserDto user,
                              @PathVariable String role) {
        return registerService.register(user, role);
    }

}
