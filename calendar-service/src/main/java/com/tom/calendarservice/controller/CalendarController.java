package com.tom.calendarservice.controller;

import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.service.CalendarService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@AllArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private static Logger logger = LoggerFactory.getLogger(CalendarController.class);
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
    public Calendar addLesson(@RequestBody Calendar calendar) {
        logger.info("Try add new lesson.");
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

    @DeleteMapping("/delete-course-lessons/{courseId}")
    public void deleteCourseLessons(@PathVariable String courseId) {
       calendarService.deleteCourseLessons(courseId);
    }

    @GetMapping("/number-course-lessons/{courseId}")
    public int getLessonsNumberByCourseId(@PathVariable String courseId) {
        return calendarService.getLessonsNumberByCourseId(courseId);
    }

    @PostMapping("/enroll-lessons/{courseId}/{studentId}")
    public ResponseEntity<?> enrollStudent(@PathVariable String courseId, @PathVariable Long studentId){
            calendarService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/un-enroll-lessons/{courseId}/{studentId}")
    public boolean unEnrollStudent(@PathVariable String courseId, @PathVariable Long studentId){
        return calendarService.unEnrollStudent(courseId, studentId);
    }

    @PostMapping("/enroll-student-lessons/{lessonId}/{studentId}")
    public ResponseEntity<?> enrollStudentLesson(@PathVariable String lessonId, @PathVariable Long studentId){
        calendarService.enrollStudentLesson(lessonId, studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/un-enroll-student-lessons/{lessonId}/{studentId}")
    public ResponseEntity<?> unEnrollStudentLesson(@PathVariable String lessonId, @PathVariable Long studentId){
        calendarService.unEnrollStudentLesson(lessonId, studentId);
        return ResponseEntity.ok().build();
    }
}
