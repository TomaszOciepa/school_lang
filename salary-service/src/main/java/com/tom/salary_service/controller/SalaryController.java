package com.tom.salary_service.controller;


import com.tom.salary_service.service.SalaryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @PreAuthorize("hasRole('admin') or hasRole('teacher')")
    @GetMapping("/create-salary/{id}")
    public void createSalary(@PathVariable Long id) {
       salaryService.createSalary(id);
    }
}
