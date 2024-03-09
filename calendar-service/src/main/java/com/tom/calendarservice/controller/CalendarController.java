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
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@AllArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private static Logger logger = LoggerFactory.getLogger(CalendarController.class);
    private final CalendarService calendarService;

    //sprawdzone
    @GetMapping
    public List<Calendar> getAllLessons() {
        logger.info("Get method getAllLessons()");
        return calendarService.getAllLessons();
    }

    @GetMapping("/{id}")
    public Calendar getLessonById(@PathVariable String id) {
        logger.info("Get method getLessonById()");
        return calendarService.getLessonById(id);
    }

    @GetMapping("/course-lessons/{courseId}")
    public List<Calendar> getLessonsByCourseId(@PathVariable String courseId) {
        logger.info("Get method getLessonsByCourseId() courseId: {}", courseId);
        return calendarService.getLessonsByCourseId(courseId);
    }

    @GetMapping("/number-course-lessons/{courseId}")
    public int getLessonsNumberByCourseId(@PathVariable String courseId) {
        logger.info("Get method getLessonsNumberByCourseId()");
        return calendarService.getLessonsNumberByCourseId(courseId);
    }

    @GetMapping("/teacher-lessons/{teacherId}")
    public List<Calendar> getLessonByTeacherId(@PathVariable Long teacherId) {
        return calendarService.getLessonsByTeacherId(teacherId);
    }

    @DeleteMapping("/delete-course-lessons/{courseId}")
    public void deleteCourseLessons(@PathVariable String courseId) {
        logger.info("Delete method deleteCourseLessons()");
        calendarService.deleteCourseLessons(courseId);
    }

    @PostMapping
    public Calendar addLesson(@RequestBody Calendar calendar) {
        logger.info("Post method addLesson().");
        return calendarService.addLesson(calendar);
    }

    @PatchMapping("/{id}")
    public Calendar patchLesson(@PathVariable String id, @RequestBody Calendar calendar) {
        logger.info("Patch method patchLesson().");
        return calendarService.patchLesson(id, calendar);
    }

    @DeleteMapping("/{id}")
    public void deleteLessonsById(@PathVariable String id) {
        logger.info("Delete method deleteLessonsById().");
        calendarService.deleteLessonsById(id);
    }

    @GetMapping("/check-teacher-assignment/{courseId}/{teacherId}")
    public boolean isTeacherAssignedToLessonInCourse(@PathVariable String courseId, @PathVariable Long teacherId) {
        logger.info("Get method isTeacherAssignedToLessonInCourse().");
        return calendarService.isTeacherAssignedToLessonInCourse(courseId, teacherId);
    }

    @PostMapping("/enroll-lessons/{courseId}/{studentId}")
    public ResponseEntity<?> enrollStudent(@PathVariable String courseId, @PathVariable Long studentId) {
        logger.info("Post method enrollStudent().");
        calendarService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/un-enroll-lessons/{courseId}/{studentId}")
    public boolean unEnrollStudent(@PathVariable String courseId, @PathVariable Long studentId) {
        logger.info("Get method unEnrollStudent().");
        return calendarService.unEnrollStudent(courseId, studentId);
    }

    @PostMapping("/enroll-student-lessons/{lessonId}/{studentId}")
    public ResponseEntity<?> enrollStudentLesson(@PathVariable String lessonId, @PathVariable Long studentId) {
        logger.info("Post method enrollStudentLesson().");
        calendarService.enrollStudentLesson(lessonId, studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/un-enroll-student-lessons/{lessonId}/{studentId}")
    public ResponseEntity<?> unEnrollStudentLesson(@PathVariable String lessonId, @PathVariable Long studentId) {
        logger.info("Post method unEnrollStudentLesson().");
        calendarService.unEnrollStudentLesson(lessonId, studentId);
        return ResponseEntity.ok().build();
    }

    //    nie sprawdzone
    @GetMapping("/student-lessons/{studentId}")
    public List<Calendar> getLessonsByStudentId(@PathVariable Long studentId) {
        return calendarService.getLessonsByStudentId(studentId);
    }
}
