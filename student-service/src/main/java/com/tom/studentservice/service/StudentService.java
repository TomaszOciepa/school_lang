package com.tom.studentservice.service;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents(Status status);
    Student getStudentById(Long id);
    Student getStudentByEmail(String email);
    Student addStudent(Student student);
    Student putStudent(Long id, Student student);
    Student patchStudent(Long id, Student student);
    void deleteStudent(Long id);
    void courseEnrollment(Long studentId, String courseName);
    void courseUnEnrollStudent(Long studentId, String courseName);
    List<Student> getStudentsByEmails(List<String> emails);

}
