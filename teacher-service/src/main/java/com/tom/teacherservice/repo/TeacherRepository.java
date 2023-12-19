package com.tom.teacherservice.repo;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    List<Teacher> findAllByStatus(Status status);

    Optional<Teacher> findByEmail(String email);

    boolean existsByEmail(String email);
    List<Teacher> findAllByIdIn(List<Long> idNumbers);
}
