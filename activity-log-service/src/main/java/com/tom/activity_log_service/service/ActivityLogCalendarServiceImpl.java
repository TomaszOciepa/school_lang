package com.tom.activity_log_service.service;

import com.tom.activity_log_service.model.ActivityLogCalendar;
import com.tom.activity_log_service.repo.ActivityLogCalendarRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogCalendarServiceImpl implements ActivityLogCalendarService {

    private static Logger logger = LoggerFactory.getLogger(ActivityLogCalendarServiceImpl.class);

    private final ActivityLogCalendarRepository repository;

    public ActivityLogCalendarServiceImpl(ActivityLogCalendarRepository repository) {
        this.repository = repository;
    }

    @Override
    @RabbitListener(queues = "create-calendar-log")
    public void createLog(ActivityLogCalendar log) {
        logger.info("createLog()");
        repository.save(log);
    }

    @Override
    public List<ActivityLogCalendar> getLogByCalendarId(String id) {
        logger.info("getLogByCalendarId()");
        return repository.findByCalendarId(id, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    @Override
    public void deleteLogByCalendarId(String id) {
        logger.info("deleteLogByCalendarId()");
        repository.deleteByCalendarId(id);
    }

    @Override
    public void dropAll() {
        logger.info("dropAll()");
        repository.deleteAll();
    }
}
