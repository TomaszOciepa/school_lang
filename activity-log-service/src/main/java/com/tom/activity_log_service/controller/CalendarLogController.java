package com.tom.activity_log_service.controller;

import com.tom.activity_log_service.model.ActivityLogCalendar;
import com.tom.activity_log_service.service.ActivityLogCalendarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar-log")

public class CalendarLogController {

    private final ActivityLogCalendarService service;

    public CalendarLogController(ActivityLogCalendarService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    List<ActivityLogCalendar> getLogByCalendarId(@PathVariable String id) {
        return service.getLogByCalendarId(id);
    }


    @DeleteMapping("/{id}")
    void deleteLogByCalendarId(@PathVariable String id) {
        service.deleteLogByCalendarId(id);
    }

    @DeleteMapping("/drop-all")
    void dropAll() {
        service.dropAll();
    }
}
