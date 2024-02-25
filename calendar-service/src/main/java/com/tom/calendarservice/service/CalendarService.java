package com.tom.calendarservice.service;


import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Dto.CourseDto;
import com.tom.calendarservice.model.Status;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CalendarService {

    List<Calendar> getAllLessons();

    Calendar getLessonById(String id);

    Calendar addLesson(Calendar calendar);

    Calendar putLesson(String id, Calendar calendar);

    Calendar patchLesson(String id, Calendar calendar);

    void deleteLesson(String id);

//    Calendar addEvent(EventRequest eventRequest);
    List<Calendar> getLessonsByStudentId(Long studentId);
    List<Calendar> getLessonsByTeacherId(Long teacherId);
    List<Calendar> getLessonsByCourseId(String courseId);
    int getLessonsNumberByCourseId(String courseId);
    void enrollStudent( String courseId,  Long studentId);
    void deleteCourseLessons(String courseId);
    public boolean unEnrollStudent(String courseId, Long studentId);
    void enrollStudentLesson(String lessonId, Long studentId);
    void unEnrollStudentLesson(String lessonId, Long studentId);
    boolean isTeacherAssignedToLessonInCourse(String courseId, Long teacherId);



}
