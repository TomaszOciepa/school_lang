package com.tom.payment_service.service;

import com.tom.payment_service.model.dto.CourseDto;
import com.tom.payment_service.model.dto.Status;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "COURSE-SERVICE")
public interface CourseServiceClient {

    @GetMapping("/course/{id}")
    CourseDto getCourseById(@PathVariable String id, @RequestParam(required = false) Status status);

}