package com.tom.teacherservice.controller;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import com.tom.teacherservice.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@AllArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public String all() {
        return "Hello teacher";
    }

//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public List<Teacher> getAllTeacher(@RequestParam(required = false) Status status) {
        return teacherService.getAllTeacher(status);
    }


    @GetMapping("/id/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @GetMapping("/email")
    public Teacher getTeacherByEmail(@RequestParam String email) {
        return teacherService.getTeacherByEmail(email);
    }

    @PostMapping
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

    @PutMapping("/{id}")
    public Teacher putTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        return teacherService.putTeacher(id, teacher);
    }

    @PatchMapping("/{id}")
    public Teacher patchTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        return teacherService.patchTeacher(id, teacher);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id){
        teacherService.deleteTeacher(id);
    }

    @PostMapping("/idNumbers")
    public List<Teacher> getTeachersByIdNumber(@RequestBody List<Long> idNumbers) {
        return teacherService.getTeachersByIdNumber(idNumbers);
    }

}
