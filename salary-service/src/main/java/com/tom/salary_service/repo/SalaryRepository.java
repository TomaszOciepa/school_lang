package com.tom.salary_service.repo;

import com.tom.salary_service.model.Salary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SalaryRepository extends MongoRepository<Salary, String> {

    List<Salary> findByTeacherIdOrderByDateAsc(Long teacherId);
}
