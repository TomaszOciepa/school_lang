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
@CrossOrigin( origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@AllArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    private static Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public List<Teacher> getAllTeacher(@RequestParam(required = false) Status status) {
        return teacherService.getAllTeacher(status);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/email")
    public Teacher getTeacherByEmail(@RequestParam String email) {
        return teacherService.getTeacherByEmail(email);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public Teacher putTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        return teacherService.putTeacher(id, teacher);
    }

    @PreAuthorize("hasRole('admin')")
    @PatchMapping("/{id}")
    public Teacher patchTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        return teacherService.patchTeacher(id, teacher);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id){
        teacherService.deleteTeacher(id);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/idNumbers")
    public List<Teacher> getTeachersByIdNumber(@RequestBody List<Long> idNumbers) {
        return teacherService.getTeachersByIdNumber(idNumbers);
    }

    @PostMapping("/notIdNumbers")
    public List<Teacher> getTeachersByNotIdNumber(@RequestBody List<Long> idNumbers) {
        return teacherService.findAllByIdNotInAndStatus(idNumbers);
    }

    @GetMapping("/teacherIsActive/{teacherId}")
    public void teacherIsActive(@PathVariable Long teacherId){
        logger.info("teacherIsActive() teacherId: "+teacherId);
        teacherService.getTeacherById(teacherId);
    }
}
