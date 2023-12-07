package com.tom.teacherservice.service;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;

import java.util.List;

public interface TeacherService {

    List<Teacher> getAllTeacher(Status status);

    Teacher getTeacherById(Long id);

    Teacher getTeacherByEmail(String email);

    Teacher addTeacher(Teacher teacher);

    Teacher putTeacher(Long id, Teacher teacher);

    Teacher patchTeacher(Long id, Teacher teacher);

    void deleteTeacher(Long id);

    void courseEnrollment(Long teacherId, String courseName);

    void courseUnEnrollTeacher(Long teacherId, String courseName);
}
