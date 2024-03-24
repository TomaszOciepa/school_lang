package com.tom.teacherservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COURSE-SERVICE")
public interface CourseServiceClient {

    @DeleteMapping("/course/remove-teacher/{teacherId}")
    ResponseEntity<?> removeTeacherWithAllCourses(@PathVariable Long teacherId);
}
