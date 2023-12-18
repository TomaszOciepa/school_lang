package com.tom.studentservice.repo;

import com.tom.studentservice.model.MyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyCourseRepository extends JpaRepository<MyCourse, Long> {

    @Query("SELECT e FROM MyCourse e WHERE e.student.id = :studentId")
    List<MyCourse> findByStudentId(@Param("studentId")Long studentId);

}
