package com.tom.calendarservice.repo;

import com.tom.calendarservice.model.Calendar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CalendarRepository extends MongoRepository<Calendar, String> {

    //sprawdzone
    @Query("{'courseId': ?0}")
    List<Calendar> getLessonsByCourseId(String courseId);

    @Query("{'teacherId': ?0}")
    List<Calendar> getLessonsByTeacherId(Long teacherId);
    //nie sprawdzone

    @Query("{'studentIdList': ?0}")
    List<Calendar> getLessonsByStudentId(Long studentId);


}
