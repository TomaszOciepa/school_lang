package com.tom.calendarservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TEACHER-SERVICE")
public interface TeacherServiceClient {

    @GetMapping("/teacher/teacher-is-active/{teacherId}")
    void teacherIsActive(@PathVariable Long teacherId);

}
