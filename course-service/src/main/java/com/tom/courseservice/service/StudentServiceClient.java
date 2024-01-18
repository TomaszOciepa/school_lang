package com.tom.courseservice.service;

import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.security.KeycloakRoleConverter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @GetMapping("/student/{id}")
    StudentDto getStudentById(@PathVariable Long id);

    @PostMapping("/student/idNumbers")
    List<StudentDto> getStudentsByIdNumber(@RequestBody List<Long> idNumbers);
}
