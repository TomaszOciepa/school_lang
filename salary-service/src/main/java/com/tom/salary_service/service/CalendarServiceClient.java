package com.tom.salary_service.service;


import com.tom.salary_service.model.dto.CalendarDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@FeignClient(name = "CALENDAR-SERVICE")
public interface CalendarServiceClient {

    @GetMapping("/calendar/teacher-lessons/{teacherId}")
    List<CalendarDto> getLessonByTeacherId(@PathVariable Long teacherId);

}
