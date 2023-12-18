package com.tom.studentservice.controller;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents(@RequestParam(required = false) Status status) {
        return studentService.getAllStudents(status);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/email")
    public Student getStudentByEmail(@RequestParam String email) {
        return studentService.getStudentByEmail(email);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public Student putStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.putStudent(id, student);
    }

    @PatchMapping("/{id}")
    public Student patchStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.patchStudent(id, student);
    }

    @DeleteMapping
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @PostMapping("/emails")
    public List<Student> getStudentsByEmails(@RequestBody List<String> emails) {
        return studentService.getStudentsByEmails(emails);
    }
    @PostMapping("/enroll/{studentId}/course/{courseName}")
    public void courseEnrollment(@PathVariable Long studentId, @PathVariable String courseName){
        studentService.courseEnrollment(studentId, courseName);
    }
}
