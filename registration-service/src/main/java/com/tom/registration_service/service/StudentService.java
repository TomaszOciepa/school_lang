package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;

public interface StudentService {

    UserDto createAccount(UserDto user);
}