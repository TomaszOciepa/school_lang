package com.tom.courseservice.service;

import com.tom.courseservice.model.dto.TeacherDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TEACHER-SERVICE")
public interface TeacherServiceClient {

    @GetMapping("/teacher/{id}")
     TeacherDto getTeacherById(@PathVariable Long id);
}
