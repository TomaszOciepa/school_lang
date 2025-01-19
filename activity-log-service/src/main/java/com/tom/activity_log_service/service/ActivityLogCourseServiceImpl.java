package com.tom.activity_log_service.service;

import com.tom.activity_log_service.model.ActivityLogCourse;
import com.tom.activity_log_service.repo.ActivityLogCourseRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogCourseServiceImpl implements ActivityLogCourseService {

    private static Logger logger = LoggerFactory.getLogger(ActivityLogCourseServiceImpl.class);
    private final ActivityLogCourseRepository repository;

    public ActivityLogCourseServiceImpl(ActivityLogCourseRepository repository) {
        this.repository = repository;
    }

    @Override
    @RabbitListener(queues = "create-course-log")
    public void createLog(ActivityLogCourse log) {
        logger.info("createLog() {}", log.toString());
        repository.save(log);
        System.out.println("Wszystko: ");
        repository.findAll().stream()
                .forEach(System.out::println);

    }

    @Override
    public List<ActivityLogCourse> getLogByCourseId(String id) {
        logger.info("getLogByCourseId()");
        return repository.findByCourseId(id, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    @Override
    public void deleteLogByCourseId(String id) {
        logger.info("deleteLogByCourseId()");
        repository.deleteByCourseId(id);
    }

    @Override
    public void dropAll() {
        logger.info("dropAll()");
        repository.deleteAll();
    }
}
