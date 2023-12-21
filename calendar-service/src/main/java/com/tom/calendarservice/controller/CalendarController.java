package com.tom.calendarservice.controller;

import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.EventRequest;
import com.tom.calendarservice.service.CalendarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping
    public List<Calendar> getAllLessons() {
        return calendarService.getAllLessons();
    }

    @GetMapping("/{id}")
    public Calendar getLessonById(@PathVariable String id) {
        return calendarService.getLessonById(id);
    }

    @PostMapping
    public Calendar addLesson(Calendar calendar) {
        return calendarService.addLesson(calendar);
    }

    @PutMapping("/{id}")
    public Calendar putLesson(@PathVariable String id, @RequestBody Calendar calendar) {
        return calendarService.putLesson(id, calendar);
    }

    @PatchMapping("/{id}")
    public Calendar patchLesson(@PathVariable String id, @RequestBody Calendar calendar) {
        return calendarService.patchLesson(id, calendar);
    }

    @DeleteMapping("/{id}")
    public void deleteLessonsById(@PathVariable String id) {
        calendarService.deleteLesson(id);
    }

    @PostMapping("/add-event")
    public Calendar addEvent(@RequestBody EventRequest eventRequest){
        return calendarService.addEvent(eventRequest);
    }

    @GetMapping("/student-lessons/{studentId}")
    public List<Calendar> getLessonByStudentId(@PathVariable Long studentId) {
        return calendarService.getLessonsByStudentId(studentId);
    }

    @GetMapping("/teacher-lessons/{teacherId}")
    public List<Calendar> getLessonByTeacherId(@PathVariable Long teacherId) {
        return calendarService.getLessonsByTeacherId(teacherId);
    }

    @GetMapping("/course-lessons/{courseId}")
    public List<Calendar> getLessonsByCourseId(@PathVariable String courseId) {
        return calendarService.getLessonsByCourseId(courseId);
    }
}
