package com.tom.studentservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CALENDAR-SERVICE")
public interface CalendarServiceClient {

    @DeleteMapping("/calendar/remove/{id}")
    void deactivateStudent(@PathVariable Long id);

    @DeleteMapping("/calendar/remove-student-with-lessons/{id}")
   void deleteStudentWithAllLessons(@PathVariable Long id);
}