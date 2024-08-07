package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;

public interface TeacherService {
    UserDto createAccount(UserDto userDto);
}
