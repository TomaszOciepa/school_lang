package com.tom.studentservice.service;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.repo.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents(Status status) {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return null;
    }

    @Override
    public Student getStudentByEmail(String email) {
        return null;
    }

    @Override
    public Student addStudent(Student student) {
        return null;
    }

    @Override
    public Student putStudent(Long id, Student student) {
        return null;
    }

    @Override
    public Student patchStudent(Long id, Student student) {
        return null;
    }

    @Override
    public void deleteStudent(Long id) {

    }

    @Override
    public void courseEnrollment(Long StudentId, String courseName) {

    }

    @Override
    public void courseUnEnrollStudent(Long StudentId, String courseName) {

    }

    @Override
    public List<Student> getStudentsByEmails(List<String> emails) {
        return null;
    }
}
