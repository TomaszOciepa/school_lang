package com.tom.courseservice.service;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.CourseStudentDto;
import com.tom.courseservice.model.dto.TeacherDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface CourseService {

    //sprawdzone
    Course addCourse(Course course);
    List<Course> getAllByStatus(Status status);
    Course getCourseById(String id, Status status);
    List<CourseStudentDto> getCourseMembers(String courseId);
    List<TeacherDto> getCourseTeachers(String courseId);
    Course patchCourse(String id, Course course);

//    nie sprawdzone
    Course putCourse(String id, Course course);
    void deleteCourse(String id);
    ResponseEntity<?> studentCourseEnrollment(String courseId, Long studentId);
    ResponseEntity<?> restoreStudentToCourse(String courseId, Long studentId);
    void studentRemoveFromCourse(String courseId, Long studentId);
    void teacherCourseEnrollment(String courseId, Long teacherId);
    void teacherRemoveFromCourse(String courseId, Long teacherId);

    void changeCourseMemberStatus(String courseId, Long studentId, Status status);

}
