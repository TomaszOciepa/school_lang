package com.tom.studentservice.service;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentService {
    //sprawdzone
    List<Student> getStudentsByIdNumbers(List<Long> idNumbers);
    List<Student> getStudents(Status status);
    List<Student> getStudentsByIdNumberNotEqual(List<Long> idNumbers);
    Student getStudentById(Long id);
    //nie sprawdzone
    Student getStudentByEmail(String email);
    Student addStudent(Student student);
    Student putStudent(Long id, Student student);
    Student patchStudent(Long id, Student student);
    void deleteStudent(Long id);
    List<Student> getStudentsByEmails(List<String> emails);
}
