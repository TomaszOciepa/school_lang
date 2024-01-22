package com.tom.courseservice.service;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.model.dto.TeacherDto;

import java.util.List;

public interface CourseService {

    List<Course> findAllByStatus(Status status);
    Course getCourseById(String id);
    Course addCourse(Course course);
    Course putCourse(String id, Course course);
    Course patchCourse(String id, Course course);
    void deleteCourse(String id);
    void studentCourseEnrollment(String courseId, Long studentId);
    void studentRemoveFromCourse(String courseId, Long studentId);
    void teacherCourseEnrollment(String courseId, Long teacherId);
    void teacherRemoveFromCourse(String courseId, Long teacherId);
    List<StudentDto> getCourseMembers(String courseId);
    List<TeacherDto> getCourseTeachers(String courseId);
    void isLessonAdditionPossible(String courseId);
}
