package com.tom.teacherservice.service;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TeacherService {

    //    sprawdzone
    List<Teacher> getTeachersByIdNumber(List<Long> idNumbers);

    Teacher getTeacherById(Long id);

    void teacherIsActive(Long id);

    List<Teacher> getTeachers(Status status);

    List<Teacher> getTeachersByIdNumberNotEqual(List<Long> idNumbers);

    Teacher addTeacher(Teacher teacher);

    ResponseEntity<Void> patchTeacher(Long id, Teacher teacher);

    void restoreTeacherAccount(Long id);

    void deactivateTeacherById(Long id);

    void deleteTeacherById(Long id);

    Teacher getTeacherByEmail(String email);
    //    nie sprawdzone


    Teacher putTeacher(Long id, Teacher teacher);


}
