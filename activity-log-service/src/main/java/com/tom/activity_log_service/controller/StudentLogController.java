package com.tom.activity_log_service.controller;

import com.tom.activity_log_service.model.ActivityLogStudent;
import com.tom.activity_log_service.service.ActivityLogStudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-log")
public class StudentLogController {

    private final ActivityLogStudentService service;

    public StudentLogController(ActivityLogStudentService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    List<ActivityLogStudent> getLogByStudentId(@PathVariable String id) {
        return service.getLogByStudentId(id);
    }

    @DeleteMapping("/{id}")
    void deleteLogByStudentId(@PathVariable String id) {
        service.deleteLogByStudentId(id);
    }

    @DeleteMapping("/drop-all")
    void dropAll() {
        service.dropAll();
    }
}
