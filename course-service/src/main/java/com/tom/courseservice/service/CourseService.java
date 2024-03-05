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
    void deleteCourseById(String id);
    void assignTeacherToCourse(String courseId, Long teacherId);
    void teacherCourseUnEnrollment(String courseId, Long teacherId);
    ResponseEntity<?> assignStudentToCourse(String courseId, Long studentId);
//    nie sprawdzone
    Course putCourse(String id, Course course);
    ResponseEntity<?> restoreStudentToCourse(String courseId, Long studentId);
    void studentRemoveFromCourse(String courseId, Long studentId);

    void changeCourseMemberStatus(String courseId, Long studentId, Status status);

}
