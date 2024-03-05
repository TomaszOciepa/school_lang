package com.tom.courseservice.controller;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.CourseStudentDto;
import com.tom.courseservice.model.dto.TeacherDto;
import com.tom.courseservice.service.CourseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private static Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;


    // sprawdzone
    @PostMapping
    Course addCourse(@RequestBody Course course){
        logger.info("Post method addCourse().");
        return courseService.addCourse(course);
    }

    @GetMapping
        List<Course> getAllByStatus(@RequestParam(required = false) Status status) {
        logger.info("Get method getAllByStatus().");
        return courseService.getAllByStatus(status);
    }

    @GetMapping("/{id}")
    Course getCourseById(@PathVariable String id, @RequestParam(required = false) Status status){
        logger.info("Get method getCourseById()");
        return courseService.getCourseById(id, status);
    }

    @GetMapping("/members/{courseId}")
    public List<CourseStudentDto> getCourseMembers(@PathVariable String courseId){
        logger.info("Get method getCourseMembers()");
        return courseService.getCourseMembers(courseId);
    }

    @GetMapping("/teacher/{courseId}")
    public List<TeacherDto> getCourseTeachers(@PathVariable String courseId){
        logger.info("Get method getCourseTeachers()");
        return courseService.getCourseTeachers(courseId);
    }

    @PatchMapping("/{id}")
    Course patchCourse(@PathVariable String id, @RequestBody Course course){
        logger.info("Patch method patchCourse()");
        return courseService.patchCourse(id, course);
    }
//    nie sprawdzone


    @PutMapping("/{id}")
    Course putCourse(@PathVariable String id, @RequestBody Course course){
        return courseService.putCourse(id, course);
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

    @PostMapping("/restore/{courseId}/student/{studentId}")
    public ResponseEntity<?> restoreStudentToCourse(@PathVariable String courseId, @PathVariable Long studentId){
        courseService.restoreStudentToCourse(courseId, studentId);

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

    @PostMapping("/member-status/course/{courseId}/student/{studentId}/status")
    public ResponseEntity<?> changeCourseMemberStatus(@PathVariable String courseId, @PathVariable Long studentId, @RequestParam(required = true) Status status){
        courseService.changeCourseMemberStatus(courseId, studentId, status);
        return ResponseEntity.ok().build();
    }
}
