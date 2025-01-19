package com.tom.activity_log_service.repo;

import com.tom.activity_log_service.model.ActivityLogCalendar;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ActivityLogCalendarRepository extends MongoRepository<ActivityLogCalendar, String> {

    List<ActivityLogCalendar> findByCalendarId(String calendarId, Sort sort);

    void deleteByCalendarId(String calendarId);
}
