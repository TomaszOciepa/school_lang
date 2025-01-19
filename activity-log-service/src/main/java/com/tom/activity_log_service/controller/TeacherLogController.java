package com.tom.activity_log_service.controller;

import com.tom.activity_log_service.model.ActivityLogTeacher;
import com.tom.activity_log_service.service.ActivityLogTeacherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher-log")

public class TeacherLogController {

    private final ActivityLogTeacherService service;

    public TeacherLogController(ActivityLogTeacherService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    List<ActivityLogTeacher> getLogByTeacherId(@PathVariable String id) {
        return service.getLogByTeacherId(id);
    }

    @DeleteMapping("/{id}")
    void deleteLogByTeacherId(@PathVariable String id) {
        service.deleteLogByTeacherId(id);
    }

    @DeleteMapping("/drop-all")
    void dropAll() {
        service.dropAll();
    }
}
