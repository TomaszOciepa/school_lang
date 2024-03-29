package com.tom.studentservice.service;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;

import java.util.List;

public interface StudentService {
    //sprawdzone
    List<Student> getStudentsByIdNumbers(List<Long> idNumbers);
    List<Student> getStudents(Status status);
    List<Student> getStudentsByIdNumberNotEqual(List<Long> idNumbers);
    Student getStudentById(Long id);
    Student addStudent(Student student);
    Student patchStudent(Long id, Student student);
    void deleteStudent(Long id);

    //nie sprawdzone
    Student getStudentByEmail(String email);
    List<Student> getStudentsByEmails(List<String> emails);
}
