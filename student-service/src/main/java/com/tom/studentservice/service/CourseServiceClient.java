package com.tom.studentservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COURSE-SERVICE")
public interface CourseServiceClient {

    @DeleteMapping("/course/deactivate-student/{studentId}")
    ResponseEntity<?> deactivateStudent(@PathVariable Long studentId);

    @DeleteMapping("/course/remove-student/{studentId}")
    ResponseEntity<?> removeStudentWithAllCourses(@PathVariable Long studentId);
}
