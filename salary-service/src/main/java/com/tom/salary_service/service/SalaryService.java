package com.tom.salary_service.service;

import com.tom.salary_service.model.Salary;

import java.util.List;

public interface SalaryService {

    void createSalary(Long teacherId);
    List<Salary> getSalaryByTeacherId(Long teacherId);
    void deleteSalaryById(String id);
    Salary saveSalary(Salary salary);
}
