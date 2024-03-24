package com.tom.courseservice.service;

import com.tom.courseservice.model.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @PostMapping("/student/idNumbers")
    List<StudentDto> getStudentsByIdNumbers(@RequestBody List<Long> idNumbers);
    @GetMapping("/student/{id}")
    StudentDto getStudentById(@PathVariable Long id);

    @GetMapping("/student/student-is-active/{studentId}")
    void studentIsActive(@PathVariable Long studentId);
}
