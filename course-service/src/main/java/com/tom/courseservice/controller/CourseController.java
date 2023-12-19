package com.tom.courseservice.controller;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.model.dto.TeacherDto;
import com.tom.courseservice.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    List<Course> findAllByStatus(@RequestParam(required = false) Status status) {
     return courseService.findAllByStatus(status);
    }

    @GetMapping("/{id}")
    Course getCourseById(@PathVariable String id){
        return courseService.getCourseById(id);
    }

    @PostMapping
    Course addCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }

    @PutMapping("/{id}")
    Course putCourse(@PathVariable String id, @RequestBody Course course){
        return courseService.putCourse(id, course);
    }

    @PatchMapping("/{id}")
    Course patchCourse(@PathVariable String id, @RequestBody Course course){
        return courseService.patchCourse(id, course);
    }

    @DeleteMapping("/{id}")
    void deleteCourse(@PathVariable String id){
        courseService.deleteCourse(id);
    }

    @PostMapping("/{courseId}/student/{studentId}")
    public ResponseEntity<?> studentCourseEnrollment(@PathVariable String courseId, @PathVariable Long studentId){
        courseService.studentCourseEnrollment(courseId, studentId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/student/{studentId}")
    public ResponseEntity<?> studentCourseUnEnrollment(@PathVariable String courseId, @PathVariable Long studentId){
        courseService.studentRemoveFromCourse(courseId, studentId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/teacher/{teacherId}")
    public ResponseEntity<?> teacherCourseEnrollment(@PathVariable String courseId, @PathVariable Long teacherId){
        courseService.teacherCourseEnrollment(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/teacher/{teacherId}")
    public ResponseEntity<?> teacherCourseUnEnrollment(@PathVariable String courseId, @PathVariable Long teacherId){
        courseService.teacherRemoveFromCourse(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/{courseId}")
    public List<StudentDto> getCourseMembers(@PathVariable String courseId){
        return courseService.getCourseMembers(courseId);
    }

    @PostMapping("/teacher/{courseId}")
    public List<TeacherDto> getCourseTeachers(@PathVariable String courseId){
        return courseService.getCourseTeachers(courseId);
    }
}
