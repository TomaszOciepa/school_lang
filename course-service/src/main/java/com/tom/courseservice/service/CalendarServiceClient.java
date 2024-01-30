package com.tom.courseservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "CALENDAR-SERVICE")
public interface CalendarServiceClient {

    @PostMapping("/calendar/enroll-lessons/{courseId}/{studentId}")
    ResponseEntity<?> enrollStudent(@PathVariable String courseId, @PathVariable Long studentId);

    @GetMapping("/calendar/un-enroll-lessons/{courseId}/{studentId}")
    public boolean unEnrollStudent(@PathVariable String courseId, @PathVariable Long studentId);
}
