package com.tom.courseservice.service;

import com.tom.courseservice.model.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentServiceClient {

    @GetMapping("/student/{id}")
    StudentDto getStudentById(@PathVariable Long id);

    @PostMapping("/student/enroll/{studentId}/course/{courseName}")
    void courseEnrollment(@PathVariable Long studentId, @PathVariable String courseName);

    @PostMapping("/student/un-enroll/{studentId}/course/{courseName}")
    void courseUnEnrollment(@PathVariable Long studentId, @PathVariable String courseName);
}
