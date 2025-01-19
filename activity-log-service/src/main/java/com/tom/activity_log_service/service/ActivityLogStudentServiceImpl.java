package com.tom.activity_log_service.service;

import com.tom.activity_log_service.model.ActivityLogStudent;
import com.tom.activity_log_service.repo.ActivityLogStudentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogStudentServiceImpl implements ActivityLogStudentService {

    private static Logger logger = LoggerFactory.getLogger(ActivityLogStudentServiceImpl.class);
    private final ActivityLogStudentRepository repository;

    public ActivityLogStudentServiceImpl(ActivityLogStudentRepository repository) {
        this.repository = repository;
    }

    @Override
    @RabbitListener(queues = "create-student-log")
    public void createLog(ActivityLogStudent log) {
        logger.info("createLog()");
        repository.save(log);
    }

    @Override
    public List<ActivityLogStudent> getLogByStudentId(String id) {
        logger.info("getLogByStudentId()");
        return repository.findByStudentId(id,  Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    @Override
    public void deleteLogByStudentId(String id) {
        logger.info("deleteLogByStudentId()");
        repository.deleteByStudentId(id);
    }

    @Override
    public void dropAll() {
        logger.info("dropAll()");
        repository.deleteAll();
    }
}
