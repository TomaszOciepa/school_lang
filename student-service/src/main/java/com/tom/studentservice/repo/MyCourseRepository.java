package com.tom.studentservice.repo;

import com.tom.studentservice.model.MyCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyCourseRepository extends JpaRepository<MyCourse, Long> {
}
