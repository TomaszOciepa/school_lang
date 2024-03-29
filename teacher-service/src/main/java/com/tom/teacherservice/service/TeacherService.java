package com.tom.teacherservice.service;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;

import java.util.List;

public interface TeacherService {

    //    sprawdzone
    List<Teacher> getTeachersByIdNumber(List<Long> idNumbers);

    Teacher getTeacherById(Long id);

    List<Teacher> getTeachers(Status status);

    List<Teacher> getTeachersByIdNumberNotEqual(List<Long> idNumbers);

    Teacher addTeacher(Teacher teacher);

    Teacher patchTeacher(Long id, Teacher teacher);

    void deleteTeacher(Long id);

    //    nie sprawdzone
    Teacher getTeacherByEmail(String email);

    Teacher putTeacher(Long id, Teacher teacher);


}
