package com.tom.courseservice.service;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.LessonScheduleRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@FeignClient(name = "CALENDAR-SERVICE")
public interface CalendarServiceClient {

    @GetMapping("/calendar/number-course-lessons/{courseId}")
    public int getLessonsNumberByCourseId(@PathVariable String courseId);

    @DeleteMapping("/calendar/delete-course-lessons/{courseId}")
    public void deleteCourseLessons(@PathVariable String courseId);

    @GetMapping("/calendar/check-teacher-assignment/{courseId}/{teacherId}")
    public boolean isTeacherAssignedToLessonInCourse(@PathVariable String courseId, @PathVariable Long teacherId);

    @PostMapping("/calendar/enroll-lessons/{courseId}/{studentId}")
    ResponseEntity<?> enrollStudent(@PathVariable String courseId, @PathVariable Long studentId);

    @GetMapping("/calendar/un-enroll-lessons/{courseId}/{studentId}")
    public boolean unEnrollStudent(@PathVariable String courseId, @PathVariable Long studentId);

    @PostMapping("/calendar/generate-course-timetable")
    public Course generateCourseTimetable(@RequestBody LessonScheduleRequest lessonScheduleRequest);
}
