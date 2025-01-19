package com.tom.activity_log_service.repo;

import com.tom.activity_log_service.model.ActivityLogStudent;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityLogStudentRepository extends MongoRepository<ActivityLogStudent, String> {

    List<ActivityLogStudent> findByStudentId(String studentId, Sort sort);

    void deleteByStudentId(String studentId);
}
