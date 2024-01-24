package com.tom.courseservice.repo;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {

    List<Course> findAllByStatus(Status status);
    Optional<Course> findByIdAndStatus(String id, Status status);
    boolean existsByName(String name);
}
