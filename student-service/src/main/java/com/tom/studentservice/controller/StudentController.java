package com.tom.studentservice.controller;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.service.StudentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private static Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PostMapping("/idNumbers")
    public List<Student> getStudentsByIdNumbers(@RequestBody List<Long> idNumbers) {
        logger.info("Post method getStudentsByIdNumber().");
        return studentService.getStudentsByIdNumbers(idNumbers);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping
    public List<Student> getStudents(@RequestParam(required = false) Status status) {
        logger.info("Get method getStudents().");
        return studentService.getStudents(status);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PostMapping("/notIdNumbers")
    public List<Student> getStudentsByIdNumberNotEqual(@RequestBody List<Long> idNumbers) {
        logger.info("Post method getStudentsByIdNumberNotEqual().");
        return studentService.getStudentsByIdNumberNotEqual(idNumbers);
    }

        @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
        @GetMapping("/{id}")
        public Student getStudentById(@PathVariable Long id) {
            logger.info("GET method getStudentById().");
            return studentService.getStudentById(id);
        }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        logger.info("POST method addStudent()");
        return studentService.addStudent(student);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchStudent(@PathVariable Long id, @RequestBody Student student) {
        logger.info("PATCH method patchStudent().");
        return studentService.patchStudent(id, student);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/deactivate/{id}")
    public void deactivateStudentById(@PathVariable Long id) {
        logger.info("Delete method deleteStudent().");
        studentService.deactivateStudentById(id);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @DeleteMapping("/remove/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentService.deleteStudentById(id);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @GetMapping("/email")
    public Student getStudentByEmail(@RequestParam String email) {
        logger.info("Get method getStudentByEmail().");
        return studentService.getStudentByEmail(email);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping("/student-is-active/{studentId}")
    public void studentIsActive(@PathVariable Long studentId) {
        logger.info("Get method studentIsActive()");
        studentService.studentIsActive(studentId);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PatchMapping("/restore/{id}")
    public void restoreStudentAccount(@PathVariable Long id) {
        logger.info("Patch method restoreStudentAccount()");
        studentService.restoreStudentAccount(id);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PostMapping("/emails")
    public List<Student> getStudentsByEmails(@RequestBody List<String> emails) {
        return studentService.getStudentsByEmails(emails);
    }

}
