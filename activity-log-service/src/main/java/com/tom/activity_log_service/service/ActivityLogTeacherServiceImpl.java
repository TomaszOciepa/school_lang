package com.tom.activity_log_service.service;

import com.tom.activity_log_service.model.ActivityLogTeacher;
import com.tom.activity_log_service.repo.ActivityLogTeacherRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogTeacherServiceImpl implements ActivityLogTeacherService {

    private static Logger logger = LoggerFactory.getLogger(ActivityLogTeacherServiceImpl.class);
    private final ActivityLogTeacherRepository repository;

    public ActivityLogTeacherServiceImpl(ActivityLogTeacherRepository repository) {
        this.repository = repository;
    }

    @Override
    @RabbitListener(queues = "create-teacher-log")
    public void createLog(ActivityLogTeacher log) {
        logger.info("createLog()");
        repository.save(log);
    }

    @Override
    public List<ActivityLogTeacher> getLogByTeacherId(String id) {
        logger.info("getLogByTeacherId()");
        return repository.findByTeacherId(id, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    @Override
    public void deleteLogByTeacherId(String id) {
        logger.info("deleteLogByTeacherId()");
        repository.deleteByTeacherId(id);
    }

    @Override
    public void dropAll() {
        logger.info("dropAll()");
        repository.deleteAll();
    }
}
