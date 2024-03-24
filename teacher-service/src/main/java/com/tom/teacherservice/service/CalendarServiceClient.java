package com.tom.teacherservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CALENDAR-SERVICE")
public interface CalendarServiceClient {

    @DeleteMapping("/calendar/remove-teacher-lessons/{id}")
    void deleteLessonsByTeacherId(@PathVariable Long id);
}
