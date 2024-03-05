package com.tom.calendarservice.service;

import com.tom.calendarservice.model.Calendar;

import java.util.List;

public interface CalendarService {
    //sprawdzone
    List<Calendar> getAllLessons();

    List<Calendar> getLessonsByCourseId(String courseId);

    int getLessonsNumberByCourseId(String courseId);

//    nie sprawdzone

    Calendar getLessonById(String id);

    Calendar addLesson(Calendar calendar);

    Calendar putLesson(String id, Calendar calendar);

    Calendar patchLesson(String id, Calendar calendar);

    void deleteLesson(String id);

    //    Calendar addEvent(EventRequest eventRequest);
    List<Calendar> getLessonsByStudentId(Long studentId);

    List<Calendar> getLessonsByTeacherId(Long teacherId);

    void enrollStudent(String courseId, Long studentId);

    void deleteCourseLessons(String courseId);

    public boolean unEnrollStudent(String courseId, Long studentId);

    void enrollStudentLesson(String lessonId, Long studentId);

    void unEnrollStudentLesson(String lessonId, Long studentId);

    boolean isTeacherAssignedToLessonInCourse(String courseId, Long teacherId);


}
