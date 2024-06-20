package com.tom.teacherservice.controller;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import com.tom.teacherservice.service.TeacherService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@AllArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    private static Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;

    //sprawdzone
    @PostMapping("/idNumbers")
    public List<Teacher> getTeachersByIdNumber(@RequestBody List<Long> idNumbers) {
        logger.info("Post method getTeachersByIdNumber()");
        return teacherService.getTeachersByIdNumber(idNumbers);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping("/teacher-is-active/{teacherId}")
    public void teacherIsActive(@PathVariable Long teacherId) {
        logger.info("Get method teacherIsActive()");
        teacherService.teacherIsActive(teacherId);
    }


    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping
    public List<Teacher> getTeachers(@RequestParam(required = false) Status status) {
        logger.info("Get method getTeachers()");
        return teacherService.getTeachers(status);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping("/email")
    public Teacher getTeacherByEmail(@RequestParam String email) {
        logger.info("Get method getTeacherByEmail()");
        return teacherService.getTeacherByEmail(email);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/id-numbers-not-equal")
    public List<Teacher> getTeachersByIdNumberNotEqual(@RequestBody List<Long> idNumbers) {
        logger.info("Post method getTeachersByIdNumberNotEqual()");
        return teacherService.getTeachersByIdNumberNotEqual(idNumbers);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher') or hasRole('student')")
    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        logger.info("Get method getTeacherById()");
        return teacherService.getTeacherById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        logger.info("Post method addTeacher()");
        return teacherService.addTeacher(teacher);
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @PatchMapping("/{id}")
    public Teacher patchTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        logger.info("Patch method patchTeacher()");
        return teacherService.patchTeacher(id, teacher);
    }

    @PreAuthorize("hasRole('admin')")
    @PatchMapping("/restore/{id}")
    public void restoreTeacherAccount(@PathVariable Long id) {
        logger.info("Patch method restoreTeacherAccount()");
         teacherService.restoreTeacherAccount(id);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public void deactivateTeacherById(@PathVariable Long id) {
        teacherService.deactivateTeacherById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/remove/{id}")
    public void deleteTeacherById(@PathVariable Long id) {
        teacherService.deleteTeacherById(id);
    }

    //    nie sprawdzone

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public Teacher putTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        return teacherService.putTeacher(id, teacher);
    }

}
