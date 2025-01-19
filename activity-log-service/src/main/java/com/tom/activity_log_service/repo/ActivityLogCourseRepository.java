package com.tom.activity_log_service.repo;

import com.tom.activity_log_service.model.ActivityLogCourse;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityLogCourseRepository extends MongoRepository<ActivityLogCourse, String> {

    List<ActivityLogCourse> findByCourseId(String courseId, Sort sort);

    void deleteByCourseId(String courseId);
}
