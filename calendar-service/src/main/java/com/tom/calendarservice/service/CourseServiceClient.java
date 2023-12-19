package com.tom.calendarservice.service;

import com.tom.calendarservice.model.Dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COURSE-SERVICE")
public interface CourseServiceClient {

    @GetMapping("/course/{id}")
    CourseDto getCourseById(@PathVariable String id);
}
