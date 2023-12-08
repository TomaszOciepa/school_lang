package com.tom.courseservice.service;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;

import java.util.List;

public interface CourseService {

    List<Course> findAllByStatus(Status status);
    Course getCourseById(String id);
    Course addCourse(Course course);
    Course putCourse(String id, Course course);
    Course patchCourse(String id, Course course);
    void deleteCourse(String id);
    void studentCourseEnrollment(String courseId, String studentId);
    void studentRemoveFromCourse(String courseId, String studentId);
    void teacherCourseEnrollment(String courseId, String teacherId);
    void teacherRemoveFromCourse(String courseId, String teacherId);
}
