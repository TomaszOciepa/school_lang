package com.tom.courseservice.controller;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Language;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.CourseStudentDto;
import com.tom.courseservice.model.dto.TeacherDto;
import com.tom.courseservice.service.CourseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private static Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PostMapping
    Course addCourse(@RequestBody Course course) {
        logger.info("Post method addCourse().");
        return courseService.addCourse(course);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @GetMapping
    List<Course> getAllByStatus(@RequestParam(required = false) Status status) {
        logger.info("Get method getAllByStatus().");
        return courseService.getAllByStatus(status);
    }

    @GetMapping("/{id}")
    Course getCourseById(@PathVariable String id, @RequestParam(required = false) Status status) {
        logger.info("Get method getCourseById()");
        return courseService.getCourseById(id, status);
    }

    @GetMapping("/course-offering/{language}")
    List<Course> getCoursesByLanguage(@PathVariable Language language) {
        logger.info("Get method getCoursesByLanguage()");
        return courseService.getCoursesByLanguage(language);
    }
    @GetMapping("/{id}/total-amount")
    String getCourseTotalAmount(@PathVariable String id) {
        logger.info("Get method getCourseTotalAmount()");
        return courseService.getCourseTotalAmount(id);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping("/members/{courseId}")
    public List<CourseStudentDto> getCourseMembers(@PathVariable String courseId) {
        logger.info("Get method getCourseMembers()");
        return courseService.getCourseMembers(courseId);
    }

    @GetMapping("/teacher/{courseId}")
    public List<TeacherDto> getCourseTeachers(@PathVariable String courseId) {
        logger.info("Get method getCourseTeachers()");
        return courseService.getCourseTeachers(courseId);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PatchMapping("/{id}")
    Course patchCourse(@PathVariable String id, @RequestBody Course course) {
        logger.info("Patch method patchCourse()");
        return courseService.patchCourse(id, course);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/{id}")
    void deleteCourseById(@PathVariable String id) {
        logger.info("Delete method deleteCourseById()");
        courseService.deleteCourseById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/{courseId}/teacher/{teacherId}")
    public void assignTeacherToCourse(@PathVariable String courseId, @PathVariable Long teacherId) {
        logger.info("Post method assignTeacherToCourse()");
        courseService.assignTeacherToCourse(courseId, teacherId);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{courseId}/teacher/{teacherId}")
    public ResponseEntity<?> teacherCourseUnEnrollment(@PathVariable String courseId, @PathVariable Long teacherId) {
        logger.info("Delete method teacherCourseUnEnrollment()");
        courseService.teacherCourseUnEnrollment(courseId, teacherId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @PostMapping("/{courseId}/student/{studentId}")
    public ResponseEntity<?> assignStudentToCourse(@PathVariable String courseId, @PathVariable Long studentId) {
        logger.info("Post method assignStudentToCourse()");
        courseService.assignStudentToCourse(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/{courseId}/student/{studentId}")
    public ResponseEntity<?> studentCourseUnEnrollment(@PathVariable String courseId, @PathVariable Long studentId) {
        logger.info("Delete method studentCourseUnEnrollment()");
        courseService.studentCourseUnEnrollment(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/deactivate-student/{studentId}")
    public ResponseEntity<?> deactivateStudent(@PathVariable Long studentId) {
        logger.info("Delete method deactivateStudent()");
        courseService.deactivateStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PostMapping("/restore/{courseId}/student/{studentId}")
    public ResponseEntity<?> restoreStudentToCourse(@PathVariable String courseId, @PathVariable Long studentId) {
        logger.info("Post method restoreStudentToCourse()");
        courseService.restoreStudentToCourse(courseId, studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping("/teacher-courses/{teacherId}")
    public List<Course> getCourseByTeacherId(@PathVariable Long teacherId) {
        logger.info("Get method getCourseByTeacherId()");
        return courseService.getCourseByTeacherId(teacherId);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @GetMapping("/student-courses/{studentId}")
    public List<Course> getCourseByStudentId(@PathVariable Long studentId) {
        logger.info("Get method getCourseByStudentId()");
        return courseService.getCourseByStudentId(studentId);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/remove-teacher/{teacherId}")
    public ResponseEntity<?> removeTeacherWithAllCourses(@PathVariable Long teacherId) {
        logger.info("Delete method removeTeacherWithAllCourses()");
        courseService.removeTeacherWithAllCourses(teacherId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/remove-student/{studentId}")
    public ResponseEntity<?> removeStudentWithAllCourses(@PathVariable Long studentId) {
        logger.info("Delete method removeTeacherWithAllCourses()");
        courseService.removeStudentWithAllCourses(studentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('student')")
    @PostMapping("/is-student/{studentId}/enroll-course")
    public boolean isStudentEnrolledInCourse(@RequestBody Course course, @PathVariable Long studentId ){
        logger.info("Get method isStudentEnrolledInCourse()");
        return courseService.isStudentEnrolledInCourse(course, studentId);
    }

}
