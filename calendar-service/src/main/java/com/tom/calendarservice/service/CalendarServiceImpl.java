package com.tom.calendarservice.service;

import com.tom.calendarservice.controller.CalendarController;
import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Dto.CourseDto;
import com.tom.calendarservice.model.Dto.CourseStudentsDto;
import com.tom.calendarservice.model.Dto.CourseTeachersDto;
import com.tom.calendarservice.model.EventRequest;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.repo.CalendarRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private static Logger logger = LoggerFactory.getLogger(CalendarServiceImpl.class);

    private CalendarRepository calendarRepository;
    private CourseServiceClient courseServiceClient;
    private TeacherServiceClient teacherServiceClient;

    @Override
    public List<Calendar> getAllLessons() {
        return calendarRepository.findAll();
    }

    @Override
    public Calendar getLessonById(String id) {
        return calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
    }

    @Override
    public Calendar addLesson(Calendar calendar) {
        Calendar lesson;

        if (calendar.getCourseId() == null || calendar.getCourseId().isEmpty()) {

            lesson = createSingleLesson(calendar);
        } else {
            lesson = createCourseLesson(calendar);
        }
        return calendarRepository.save(lesson);
    }

    private Calendar createSingleLesson(Calendar calendar) {
        logger.info("createSingleLesson()  because courseId is: " + calendar.getCourseId());
        calendar.setStatus(Status.ACTIVE);
        return calendar;
    }

    private Calendar createCourseLesson(Calendar calendar) {
        logger.info("createCourseLesson()  because courseId is: " + calendar.getCourseId());
        isLessonAdditionPossible(calendar.getCourseId());
        isTeacherActive(calendar.getTeacherId());

        calendar.setStatus(Status.ACTIVE);
        return calendarRepository.save(calendar);
    }

    private void isLessonAdditionPossible(String courseId) {
        logger.info("isLessonAdditionPossible() courseId: " + courseId);
        courseServiceClient.isLessonAdditionPossible(courseId);
    }

    private void isTeacherActive(Long teacherId) {
        logger.info("isTeacherActive() courseId: " + teacherId);
        teacherServiceClient.teacherIsActive(teacherId);
    }

    @Override
    public Calendar putLesson(String id, Calendar calendar) {
        return calendarRepository.findById(id)
                .map(calendarFromDb -> {
                    calendarFromDb.setEventName(calendar.getEventName());
                    calendarFromDb.setStartDate(calendar.getStartDate());
                    calendarFromDb.setEndDate(calendar.getEndDate());
                    calendarFromDb.setStudentIdList(calendar.getStudentIdList());
                    calendarFromDb.setTeacherId(calendar.getTeacherId());
                    calendarFromDb.setCourseId(calendar.getCourseId());
                    calendarFromDb.setDescription(calendar.getDescription());
                    return calendarRepository.save(calendarFromDb);
                })
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
    }

    @Override
    public Calendar patchLesson(String id, Calendar calendar) {
        Calendar calendarFromDB = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));

        if (calendar.getEventName() != null) {
            calendarFromDB.setEventName(calendar.getEventName());
        }
        if (calendar.getStartDate() != null) {
            calendarFromDB.setStartDate(calendar.getStartDate());
        }
        if (calendar.getEndDate() != null) {
            calendarFromDB.setEndDate(calendar.getEndDate());
        }
        if (calendar.getStudentIdList() != null) {
            calendarFromDB.setStudentIdList(calendar.getStudentIdList());
        }
        if (calendar.getTeacherId() != null) {
            calendarFromDB.setTeacherId(calendar.getTeacherId());
        }
        if (calendar.getCourseId() != null) {
            calendarFromDB.setCourseId(calendar.getCourseId());
        }
        if (calendar.getDescription() != null) {
            calendarFromDB.setDescription(calendar.getDescription());
        }

        return calendarRepository.save(calendarFromDB);
    }

    @Override
    public void deleteLesson(String id) {
        calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
        calendarRepository.deleteById(id);
    }

//    @Override
//    public Calendar addEvent(EventRequest eventRequest) {
//        CourseDto course = courseServiceClient.getCourseById(eventRequest.getCourseId());
//        Long teacherId = course.getCourseTeachers().stream()
//                .filter(teacher -> teacher.getTeacherId().equals(eventRequest.getTeacherId()))
//                .map(CourseTeachersDto::getTeacherId)
//                .findFirst()
//                .orElseThrow(() -> new CalendarException(CalendarError.Teacher_NOT_FOUND_IN_COURSE));
//
//        List<Long> studentIdList = course.getCourseStudents().stream()
//                .map(CourseStudentsDto::getStudentId)
//                .collect(Collectors.toList());
//
//        Calendar newEvent = new Calendar(
//                course.getName(),
//                eventRequest.getStartDate(),
//                eventRequest.getEndDate(),
//                teacherId,
//                course.getId(),
//                Status.ACTIVE,
//                eventRequest.getDescription(),
//                studentIdList
//        );
//
//        return calendarRepository.save(newEvent);
//    }

    @Override
    public List<Calendar> getLessonsByStudentId(Long studentId) {
        List<Calendar> lessons = calendarRepository.getLessonsByStudentId(studentId);
        if (lessons.isEmpty()) {
            throw new CalendarException(CalendarError.CALENDAR_LESSONS_NOT_FOUND);
        }
        return lessons;
    }

    @Override
    public List<Calendar> getLessonsByTeacherId(Long teacherId) {
        List<Calendar> lessons = calendarRepository.getLessonsByTeacherId(teacherId);
        if (lessons.isEmpty()) {
            throw new CalendarException(CalendarError.CALENDAR_LESSONS_NOT_FOUND);
        }
        return lessons;
    }

    @Override
    public List<Calendar> getLessonsByCourseId(String courseId) {
        List<Calendar> lessons = calendarRepository.getLessonsByCourseId(courseId);
        if (lessons.isEmpty()) {
            throw new CalendarException(CalendarError.CALENDAR_LESSONS_NOT_FOUND);
        }
        return lessons;
    }
}
