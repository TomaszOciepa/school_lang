package com.tom.calendarservice.repo;

import com.tom.calendarservice.model.Calendar;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CalendarRepository extends MongoRepository<Calendar, String> {

    List<Calendar> findByCourseIdOrderByStartDateAsc(String courseId);

    List<Calendar> findByTeacherIdOrderByStartDateAsc(Long teacherId);


    List<Calendar> findByAttendanceListStudentIdOrderByStartDateAsc(Long studentId);

    List<Calendar> findAllByOrderByStartDateAsc();

}
