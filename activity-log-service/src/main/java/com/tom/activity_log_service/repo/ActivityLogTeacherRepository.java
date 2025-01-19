package com.tom.activity_log_service.repo;

import com.tom.activity_log_service.model.ActivityLogTeacher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityLogTeacherRepository extends MongoRepository<ActivityLogTeacher, String> {

    List<ActivityLogTeacher> findByTeacherId(String teacherId, Sort sort);

    void deleteByTeacherId(String teacherId);
}
