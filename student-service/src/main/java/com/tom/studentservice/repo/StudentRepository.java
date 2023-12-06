package com.tom.studentservice.repo;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByStatus(Status status);
}
