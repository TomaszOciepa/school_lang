package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherServiceClient teacherServiceClient;
    private final AdminAuthenticationServiceImpl adminAuthenticationService;

    public TeacherServiceImpl(TeacherServiceClient teacherServiceClient, AdminAuthenticationServiceImpl adminAuthenticationService) {
        this.teacherServiceClient = teacherServiceClient;
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @Override
    public UserDto createAccount(UserDto userDto) {
        String accessToken = adminAuthenticationService.getAccessToken();
        return teacherServiceClient.addUser(accessToken, userDto);
    }
}
