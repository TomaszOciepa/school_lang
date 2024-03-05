package com.tom.calendarservice.service;

import com.tom.calendarservice.model.Dto.CourseDto;
import com.tom.calendarservice.model.Status;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "COURSE-SERVICE")
public interface CourseServiceClient {

    @GetMapping("/course/{id}")
    CourseDto getCourseById(@PathVariable String id, @RequestParam(required = false) Status status);

}
