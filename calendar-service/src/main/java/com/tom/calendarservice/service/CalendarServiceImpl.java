package com.tom.calendarservice.service;

import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.AttendanceList;
import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Dto.CourseDto;
import com.tom.calendarservice.model.Dto.CourseStudentsDto;
import com.tom.calendarservice.model.Dto.CourseTeachersDto;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.repo.CalendarRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private static Logger logger = LoggerFactory.getLogger(CalendarServiceImpl.class);

    private final CalendarRepository calendarRepository;
    private final CourseServiceClient courseServiceClient;
    private final TeacherServiceClient teacherServiceClient;


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

        if(calendar.getStartDate().isAfter(calendar.getEndDate())){
            throw new CalendarException(CalendarError.LESSON_START_DATE_IS_AFTER_END_DATE);
        }

        if (calendar.getCourseId() == null || calendar.getCourseId().isEmpty()) {

            lesson = createSingleLesson(calendar);
        } else {
            lesson = createCourseLesson(calendar);
        }
        return calendarRepository.save(lesson);
    }

    private Calendar createSingleLesson(Calendar calendar) {
        logger.info("Creating single lesson.");
        isTeacherActive(calendar.getTeacherId());

        if (isTeacherHaveLessons(calendar.getTeacherId())) {
            List<Calendar> lessons = getLessonsByTeacherId(calendar.getTeacherId());
            isTeacherAvailableOnTimeSlot(lessons, calendar.getStartDate(), calendar.getEndDate());
        }

        calendar.setStatus(Status.ACTIVE);
        return calendar;
    }

    private Calendar createCourseLesson(Calendar calendar) {
        logger.info("Creating course lesson");

        if (isLessonExistForThisCourseInCalendar(calendar.getCourseId())) {
            isCourseHaveLessonAvailableOnTimeSlot(calendar.getCourseId(), calendar.getStartDate(), calendar.getEndDate());
        }

        CourseDto courseFromDb = getCourse(calendar.getCourseId(), Status.ACTIVE);
        isLessonStartDateBeforeCourseStartDate(calendar.getStartDate(), courseFromDb.getStartDate());
        isLessonStartDateAfterCourseEndDate(calendar.getStartDate(), courseFromDb.getEndDate());
        isTeacherEnrolledInCourse(calendar.getTeacherId(), courseFromDb.getCourseTeachers());
        isTeacherActive(calendar.getTeacherId());

        if (isTeacherHaveLessons(calendar.getTeacherId())) {
            List<Calendar> lessons = getLessonsByTeacherId(calendar.getTeacherId());
            isTeacherAvailableOnTimeSlot(lessons, calendar.getStartDate(), calendar.getEndDate());
        }

        List<AttendanceList> attendanceLists = addStudentsToAttendanceList(courseFromDb.getCourseStudents());
        calendar.setAttendanceList(attendanceLists);
        calendar.setStatus(Status.ACTIVE);
        return calendarRepository.save(calendar);
    }

    private void isLessonStartDateBeforeCourseStartDate(LocalDateTime lessonStartDate, LocalDate courseStartDate) {

        if (lessonStartDate.isBefore(courseStartDate.atStartOfDay())) {
            throw new CalendarException(CalendarError.LESSON_START_DATE_IS_BEFORE_COURSE_START_DATE);
        }

    }

    private void isLessonStartDateAfterCourseEndDate(LocalDateTime lessonStartDate, LocalDate courseEndDate) {

        if (lessonStartDate.isAfter(courseEndDate.atStartOfDay())) {
            throw new CalendarException(CalendarError.LESSON_START_DATE_IS_BEFORE_COURSE_START_DATE);
        }

    }

    private List<AttendanceList> addStudentsToAttendanceList(List<CourseStudentsDto> courseStudents) {
        if (courseStudents.isEmpty()) {
            throw new CalendarException(CalendarError.COURSE_STUDENTS_LIST_IS_EMPTY);
        } else {
            return courseStudents.stream()
                    .map(student -> {
                        AttendanceList attendance = new AttendanceList();
                        attendance.setStudentId(student.getStudentId());
                        attendance.setPresent(false);
                        return attendance;
                    }).collect(Collectors.toList());
        }
    }

    private void isTeacherEnrolledInCourse(Long teacherId, List<CourseTeachersDto> courseTeachers) {

        if (!courseTeachers.stream().anyMatch(teacherDto -> teacherDto.getTeacherId().equals(teacherId))) {
            throw new CalendarException(CalendarError.TEACHER_IS_NOT_ENROLLED_IN_COURSE);
        }
    }

    private CourseDto getCourse(String courseId, Status status) {
        return courseServiceClient.findByIdAndStatus(courseId, status);
    }

    private boolean isLessonExistForThisCourseInCalendar(String courseId) {
        if (getLessonsByCourseId(courseId).isEmpty()) {
            logger.info("No lesson exist for this course in calendar");
            return false;
        }
        logger.info("Lesson exist for this course in calendar");
        return true;
    }

    private void isTeacherActive(Long teacherId) {
        logger.info("I check if the teacher is active");
        teacherServiceClient.teacherIsActive(teacherId);
    }

    private void isCourseHaveLessonAvailableOnTimeSlot(String courseId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("I check if the course has a free date");
        List<Calendar> lessons = getLessonsByCourseId(courseId);

        for (Calendar lesson : lessons) {
            if (startDate.isBefore(lesson.getEndDate()) && endDate.isAfter(lesson.getStartDate())) {
                logger.info("Course is not available at this time slot. Lesson collision.");
                throw new CalendarException(CalendarError.COURSE_BUSY_AT_TIME_SLOT);
            }
        }
    }

    private boolean isTeacherHaveLessons(Long teacherId) {
        List<Calendar> lessons = getLessonsByTeacherId(teacherId);
        if (lessons.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void isTeacherAvailableOnTimeSlot(List<Calendar> lessons, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("I check if the teacher has a free date");

        for (Calendar lesson : lessons) {

            if (startDate.isBefore(lesson.getEndDate()) && endDate.isAfter(lesson.getStartDate())) {
                logger.info("Teacher is not available at this time slot. Lesson collision.");
                throw new CalendarException(CalendarError.TEACHER_BUSY_AT_TIME_SLOT);
            }
        }
    }

    @Override
    public Calendar putLesson(String id, Calendar calendar) {
        return calendarRepository.findById(id)
                .map(calendarFromDb -> {
                    calendarFromDb.setEventName(calendar.getEventName());
                    calendarFromDb.setStartDate(calendar.getStartDate());
                    calendarFromDb.setEndDate(calendar.getEndDate());
                    calendarFromDb.setAttendanceList(calendar.getAttendanceList());
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
        if (calendar.getAttendanceList() != null) {
            calendarFromDB.setAttendanceList(calendar.getAttendanceList());
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
            logger.info("Teacher lessons list is empty.");
        }
        return lessons;
    }

    @Override
    public List<Calendar> getLessonsByCourseId(String courseId) {
        List<Calendar> lessons = calendarRepository.getLessonsByCourseId(courseId);
        if (lessons.isEmpty()) {
            logger.info("Lessons list is empty.");
        }
        return lessons;
    }

    public void enrollStudent(String courseId, Long studentId) {
        logger.info("Adding a student to the lesson.");
        List<Calendar> lessons = getLessonsByCourseId(courseId);

        lessons.stream().map(lesson -> {

            if (lesson.getAttendanceList().stream().anyMatch(s -> s.getStudentId().equals(studentId))) {
                throw new CalendarException(CalendarError.STUDENT_ALREADY_ENROLLED);
            }

            lesson.getAttendanceList().add(new AttendanceList(studentId));
            return calendarRepository.save(lesson);
        }).collect(Collectors.toList());
    }

    public boolean unEnrollStudent(String courseId, Long studentId) {
        logger.info("Removing a student to the lesson.");
        List<Calendar> lessons = getLessonsByCourseId(courseId);

        if (lessons.isEmpty()) {
            return false;
        }

        boolean result = false;

        for (Calendar lesson : lessons) {

            if (lesson.getStartDate().isBefore(LocalDateTime.now()) && !result) {
//                If the lesson has already taken place, return true
                result = true;
            } else if (lesson.getStartDate().isAfter(LocalDateTime.now())){
                lesson.getAttendanceList().removeIf(s -> s.getStudentId().equals(studentId));
                calendarRepository.save(lesson);
            }
        }

        return result;
    }

    public void enrollStudentLesson(String lessonId, Long studentId){
        Calendar lessonFromDb = getLessonById(lessonId);

        if(lessonFromDb.getAttendanceList().stream().anyMatch(s -> s.getStudentId().equals(studentId))){
            throw new CalendarException(CalendarError.STUDENT_ALREADY_ENROLLED);
        }

        lessonFromDb.getAttendanceList().add(new AttendanceList(studentId));
        calendarRepository.save(lessonFromDb);
    }

    public void unEnrollStudentLesson(String lessonId, Long studentId){
        Calendar lessonFromDb = getLessonById(lessonId);

        lessonFromDb.getAttendanceList().removeIf(s -> s.getStudentId().equals(studentId));
        calendarRepository.save(lessonFromDb);
    }
}
