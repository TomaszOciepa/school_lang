package com.tom.calendarservice.service;


import com.tom.calendarservice.model.Calendar;

import java.util.List;

public interface CalendarService {

    List<Calendar> getAllLessons();

    Calendar getLessonById(String id);

    Calendar addLesson(Calendar calendar);

    Calendar putLesson(String id, Calendar calendar);

    Calendar patchLesson(String id, Calendar calendar);

    void deleteLesson(String id);


}
