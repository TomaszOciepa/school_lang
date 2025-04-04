package com.tom.studentservice.service;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {

    List<Student> getStudentsByIdNumbers(List<Long> idNumbers);
    List<Student> getStudents(Status status);
    List<Student> getStudentsByIdNumberNotEqual(List<Long> idNumbers);
    Student getStudentById(Long id);
    Student getStudentByEmail(String email);
    Student addStudent(Student student);
    ResponseEntity<Void> patchStudent(Long id, Student student);
    void deactivateStudentById(Long id);
    void deleteStudentById(Long id);
    void studentIsActive(Long id);
    void restoreStudentAccount(Long id);
    List<Student> getStudentsByEmails(List<String> emails);
}
