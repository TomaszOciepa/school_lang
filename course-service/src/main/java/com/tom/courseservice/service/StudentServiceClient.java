package com.tom.courseservice.service;

import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.security.KeycloakRoleConverter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {
    //    sprawdzone
    @PostMapping("/student/idNumbers")
    List<StudentDto> getStudentsByIdNumbers(@RequestBody List<Long> idNumbers);

    //    nie sprawdzone

    @GetMapping("/student/{id}")
    StudentDto getStudentById(@PathVariable Long id);

}
