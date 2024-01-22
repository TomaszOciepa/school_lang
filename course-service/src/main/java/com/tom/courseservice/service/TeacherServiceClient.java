package com.tom.courseservice.service;

import com.tom.courseservice.model.dto.TeacherDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "TEACHER-SERVICE")
public interface TeacherServiceClient {

    @GetMapping("/teacher/{id}")
     TeacherDto getTeacherById(@PathVariable Long id);

    @PostMapping("/teacher/idNumbers")
    List<TeacherDto> getTeachersByIdNumber(@RequestBody List<Long> idNumbers);

    @GetMapping("/teacher/teacherIsActive/{teacherId}")
    boolean teacherIsActive(@PathVariable Long teacherId);
}
