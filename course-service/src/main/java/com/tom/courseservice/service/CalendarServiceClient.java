package com.tom.courseservice.service;


import org.apache.hc.core5.http.HttpStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "CALENDAR-SERVICE")
public interface CalendarServiceClient {

    @PostMapping("/calendar/enroll-lessons/{courseId}/{studentId}")
    ResponseEntity<?> enrollStudent(@PathVariable String courseId, @PathVariable Long studentId);
}
