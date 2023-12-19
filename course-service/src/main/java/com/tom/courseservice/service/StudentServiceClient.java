package com.tom.courseservice.service;

import com.tom.courseservice.model.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @GetMapping("/student/{id}")
    StudentDto getStudentById(@PathVariable Long id);
}
