package com.tom.order_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "COURSE-SERVICE")
public interface CourseServiceClient {

    @PostMapping("/course/{courseId}/student/{studentId}")
    ResponseEntity<?> assignStudentToCourse(@PathVariable String courseId, @PathVariable Long studentId);

    @GetMapping("/course/{id}/total-amount")
    String getCourseTotalAmount(@PathVariable String id);
}