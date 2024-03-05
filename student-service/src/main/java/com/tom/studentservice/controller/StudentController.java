package com.tom.studentservice.controller;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.service.StudentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@AllArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private static Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    //    sprawdzone
    @PostMapping("/idNumbers")
    public List<Student> getStudentsByIdNumbers(@RequestBody List<Long> idNumbers) {
        logger.info("Post method getStudentsByIdNumber().");
        return studentService.getStudentsByIdNumbers(idNumbers);
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public List<Student> getStudents(@RequestParam(required = false) Status status) {
        logger.info("Get method getStudents().");
        return studentService.getStudents(status);
    }
    @PostMapping("/notIdNumbers")
    public List<Student> getStudentsByIdNumberNotEqual(@RequestBody List<Long> idNumbers) {
        logger.info("Post method getStudentsByIdNumberNotEqual().");
        return studentService.getStudentsByIdNumberNotEqual(idNumbers);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        logger.info("GET method getStudentById().");
        return studentService.getStudentById(id);
    }
    //    nie sprawdzone
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

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @PostMapping("/emails")
    public List<Student> getStudentsByEmails(@RequestBody List<String> emails) {
        return studentService.getStudentsByEmails(emails);
    }

}
