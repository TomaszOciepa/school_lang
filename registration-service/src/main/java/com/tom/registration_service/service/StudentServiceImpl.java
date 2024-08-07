package com.tom.registration_service.service;

import com.tom.registration_service.model.UserDto;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentServiceClient studentServiceClient;
    private final AdminAuthenticationServiceImpl adminAuthenticationService;

    public StudentServiceImpl(StudentServiceClient studentServiceClient,
                              AdminAuthenticationServiceImpl adminAuthenticationService) {
        this.studentServiceClient = studentServiceClient;
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @Override
    public UserDto createAccount(UserDto student) {
        String accessToken = adminAuthenticationService.getAccessToken();
        return studentServiceClient.addUser(accessToken, student);
    }
}
