package com.tom.activity_log_service.controller;

import com.tom.activity_log_service.model.ActivityLogCourse;
import com.tom.activity_log_service.service.ActivityLogCourseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-log")

public class CourseLogController {

    private final ActivityLogCourseService service;

    public CourseLogController(ActivityLogCourseService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    List<ActivityLogCourse> getLogByCourseId(@PathVariable String id) {
        return service.getLogByCourseId(id);
    }

    @DeleteMapping("/{id}")
    void deleteLogByCourseId(@PathVariable String id) {
        service.deleteLogByCourseId(id);
    }

    @DeleteMapping("/drop-all")
    void dropAll() {
        service.dropAll();
    }
}
