package com.tom.calendarservice.repo;

import com.tom.calendarservice.model.Calendar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CalendarRepository extends MongoRepository <Calendar, String> {



}
