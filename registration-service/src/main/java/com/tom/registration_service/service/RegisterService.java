package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;

public interface RegisterService {

    UserDto register(UserDto user, String role);
}
