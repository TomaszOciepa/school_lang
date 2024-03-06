package com.tom.calendarservice.service;

import com.tom.calendarservice.model.Calendar;

import java.util.List;

public interface CalendarService {
    //sprawdzone
    List<Calendar> getAllLessons();
    Calendar getLessonById(String id);
    List<Calendar> getLessonsByCourseId(String courseId);

    int getLessonsNumberByCourseId(String courseId);

    void deleteCourseLessons(String courseId);

    Calendar addLesson(Calendar calendar);

    Calendar patchLesson(String id, Calendar calendar);
    void deleteLessonsById(String id);
    boolean isTeacherAssignedToLessonInCourse(String courseId, Long teacherId);
    void enrollStudent(String courseId, Long studentId);
    public boolean unEnrollStudent(String courseId, Long studentId);
    void enrollStudentLesson(String lessonId, Long studentId);
    void unEnrollStudentLesson(String lessonId, Long studentId);
//    nie sprawdzone

    //    Calendar addEvent(EventRequest eventRequest);
    List<Calendar> getLessonsByStudentId(Long studentId);

    List<Calendar> getLessonsByTeacherId(Long teacherId);

}
