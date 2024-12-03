package com.tom.calendarservice.service;

import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.AttendanceList;
import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Dto.CourseDto;
import com.tom.calendarservice.model.Dto.CourseStudentsDto;
import com.tom.calendarservice.model.Dto.CourseTeachersDto;
import com.tom.calendarservice.model.Dto.StudentDto;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.repo.CalendarRepository;
import com.tom.calendarservice.security.AuthenticationContext;
import com.tom.calendarservice.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private static Logger logger = LoggerFactory.getLogger(CalendarServiceImpl.class);

    private final CalendarRepository calendarRepository;
    private final CourseServiceClient courseServiceClient;
    private final TeacherServiceClient teacherServiceClient;
    private final AuthenticationContext authenticationContext;
    private final StudentServiceClient studentServiceClient;
    private final JwtUtils jwtUtils;

    //sprawdzone
    @Override
    public List<Calendar> getAllLessons() {
        logger.info("Fetching lessons.");
        return calendarRepository.findAll().stream()
                .map(this::updateLessonStatus)
                .collect(Collectors.toList());
    }

    @Override
    public Calendar getLessonById(String id) {
        logger.info("Fetching lesson by id: {}.", id);
        Calendar lesson = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_LESSONS_NOT_FOUND));
        return updateLessonStatus(lesson);
    }

    @Override
    public List<Calendar> getLessonsByCourseId(String courseId) {
        logger.info("getLessonsByCourseId courseId: {}", courseId);
        List<Calendar> lessons = calendarRepository.getLessonsByCourseId(courseId).stream()
                .map(this::updateLessonStatus)
                .collect(Collectors.toList());
        if (lessons.isEmpty()) {
            logger.info("Lessons list is empty.");
        }
        return lessons;
    }

    @Override
    public int getLessonsNumberByCourseId(String courseId) {
        logger.info("Fetching Lessons number by courseId: {}", courseId);
        return getLessonsByCourseId(courseId).size();
    }

    public void deleteCourseLessons(String courseId) {
        logger.info("Delete lessons by courseId: {}", courseId);
        logger.info("Fetching lessons list.");
        List<Calendar> lessonsList = getLessonsByCourseId(courseId);

        lessonsList.forEach(lesson -> {
            logger.info("Delete lesson id: {}", lesson.getId());
            deleteLessonsById(lesson.getId());
        });
    }

    @Override
    public Calendar addLesson(Calendar calendar) {
        Calendar lesson;
        isLessonStartDateIsAfterLessonEndDate(calendar.getStartDate(), calendar.getEndDate());

        if (calendar.getCourseId() == null || calendar.getCourseId().isEmpty()) {

            lesson = createSingleLesson(calendar);
        } else {
            lesson = createCourseLesson(calendar);
        }
        return calendarRepository.save(lesson);
    }

    @Override
    public List<Calendar> getLessonsByTeacherId(Long teacherId) {
        logger.info("Fetching lessons by teacher id.");
        List<Calendar> lessons = calendarRepository.getLessonsByTeacherId(teacherId).stream()
                .map(this::updateLessonStatus)
                .collect(Collectors.toList());
        if (lessons.isEmpty()) {
            logger.info("Teacher lessons list is empty.");
        }
        return lessons;
    }

    @Override
    public List<Calendar> getLessonsByStudentId(Long studentId) {
        logger.info("Fetching lessons by student id.");
        List<Calendar> lessons = calendarRepository.getLessonsByStudentId(studentId);
        if (lessons.isEmpty()) {
            logger.info("Teacher lessons list is empty.");
            throw new CalendarException(CalendarError.CALENDAR_LESSONS_NOT_FOUND);
        }
        return lessons;
    }

    @Override
    public Calendar patchLesson(String id, Calendar lesson) {
        logger.info("Patch lesson id: {}", id);
        Calendar lessonFromDB = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));

        if (lesson.getEventName() != null) {
            logger.info("Changing the event name...");
            lessonFromDB.setEventName(lesson.getEventName());
        }

        if (lesson.getStartDate() != null || lesson.getEndDate() != null) {
            boolean startDateChanged = lesson.getStartDate() != null;
            boolean endDateChanged = lesson.getEndDate() != null;


            Long teacherId = null;

            if (lesson.getTeacherId() == null) {
                teacherId = lessonFromDB.getTeacherId();
            } else {
                teacherId = lesson.getTeacherId();
            }

            List<Calendar> lessonsByTeacherId = getLessonsByTeacherId(teacherId);

            if (startDateChanged) {
                logger.info("Changing the lesson start date ...");
                System.out.println("godzina rozpoczecia :" + lesson.getStartDate());
                if (lesson.getEndDate() == null) {
                    lesson.setEndDate(lessonFromDB.getEndDate());
                }

                isLessonStartDateIsAfterLessonEndDate(lesson.getStartDate(), lesson.getEndDate());

                if (lessonFromDB.getCourseId() != null) {
                    CourseDto courseFromDB = courseServiceClient.getCourseById(lessonFromDB.getCourseId(), null);
                    isLessonStartDateBeforeCourseStartDate(lesson.getStartDate(), courseFromDB.getStartDate());
                    isLessonStartDateAfterCourseEndDate(lesson.getStartDate(), courseFromDB.getEndDate());
                }

                isTeacherAvailableOnTimeSlot(lessonsByTeacherId, lesson.getStartDate(), lesson.getEndDate());

                lessonFromDB.setStartDate(lesson.getStartDate());
            }

            if (endDateChanged) {
                logger.info("Changing the lesson end date ...");
                System.out.println("godzina zakonczenia :" + lesson.getEndDate());
                if (lesson.getStartDate() == null) {
                    lesson.setStartDate(lessonFromDB.getStartDate());
                }
                isLessonStartDateIsAfterLessonEndDate(lesson.getStartDate(), lesson.getEndDate());

                if (lessonFromDB.getCourseId() != null) {
                    CourseDto courseFromDB = courseServiceClient.getCourseById(lessonFromDB.getCourseId(), null);
                    isLessonEndDateBeforeCourseStartDate(lesson.getEndDate(), courseFromDB.getStartDate());
                    isLessonEndDateAfterCourseEndDate(lesson.getEndDate(), courseFromDB.getEndDate());
                }

                isTeacherAvailableOnTimeSlot(lessonsByTeacherId, lesson.getStartDate(), lesson.getEndDate());
                lessonFromDB.setEndDate(lesson.getEndDate());

            }
        }


        if (lesson.getTeacherId() != null) {
            logger.info("Changing teacher");
            isTeacherActive(lesson.getTeacherId());
            CourseDto courseFromDb = courseServiceClient.getCourseById(lessonFromDB.getCourseId(), null);
            isTeacherEnrolledInCourse(lesson.getTeacherId(), courseFromDb.getCourseTeachers());

            List<Calendar> lessonsByTeacherId = getLessonsByTeacherId(lesson.getTeacherId());

            LocalDateTime startDate = null;
            LocalDateTime endDate = null;

            if (lesson.getStartDate() == null) {
                startDate = lessonFromDB.getStartDate();
            } else {
                startDate = lesson.getStartDate();
            }

            if (lesson.getEndDate() == null) {
                endDate = lessonFromDB.getEndDate();
            } else {
                endDate = lesson.getEndDate();
            }

            isTeacherAvailableOnTimeSlot(lessonsByTeacherId, startDate, endDate);

            lessonFromDB.setTeacherId(lesson.getTeacherId());
        }

        if (lesson.getDescription() != null) {
            logger.info("Changing description");
            lessonFromDB.setDescription(lesson.getDescription());
        }

        if (lesson.getAttendanceList() != null && !lesson.getAttendanceList().isEmpty()) {
            logger.info("Changing attendance list");
            lessonFromDB.setAttendanceList(lesson.getAttendanceList());
        }
        return calendarRepository.save(lessonFromDB);
    }

    @Override
    public void deleteLessonsById(String id) {
        logger.info("Delete lesson lessonId: {}", id);
        calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
        calendarRepository.deleteById(id);
    }

    @Override
    public void deleteLessonsByTeacherId(Long id) {
        logger.info("deleteLessonsByTeacherId: {}", id);

        List<Calendar> lessons = getLessonsByTeacherId(id);

        lessons.stream()
                .map(Calendar::getId)
                .forEach(calendarRepository::deleteById);
    }

    @Override
    public boolean isTeacherAssignedToLessonInCourse(String courseId, Long teacherId) {
        logger.info("Checking isTeacherAssignedToLessonInCourse courseId: {}, teacherId: {}", courseId, teacherId);
        List<Calendar> courseLessonsList = getLessonsByCourseId(courseId);

        if (courseLessonsList.stream().anyMatch(l -> l.getTeacherId().equals(teacherId))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void enrollStudent(String courseId, Long studentId) {
        logger.info("Assign student to lesson.");
        boolean isStudent = authenticationContext();
        checkUserPermissions(studentId, isStudent);
        List<Calendar> lessons = getLessonsByCourseId(courseId);
        lessons.stream().map(lesson -> {
            if (!lesson.getAttendanceList().stream().anyMatch(s -> s.getStudentId().equals(studentId))) {
                lesson.getAttendanceList().add(new AttendanceList(studentId));
            }
            return calendarRepository.save(lesson);
        }).collect(Collectors.toList());
    }

    private void checkUserPermissions(Long studentId, boolean isStudent) {
        logger.info("checking User Permissions.");
        if (isStudent) {
            String loggedInUserEmail = jwtUtils.getUserEmailFromJwt();
            StudentDto studentDto = studentServiceClient.getStudentById(studentId);
            if (!loggedInUserEmail.equals(studentDto.getEmail()))
                throw new CalendarException(CalendarError.STUDENT_OPERATION_FORBIDDEN);
        }
    }

    @Override
    public boolean unEnrollStudent(String courseId, Long studentId) {
        logger.info("unEnrollStudent courseId: {}, studentId: {}", courseId, studentId);
        List<Calendar> lessons = getLessonsByCourseId(courseId);

        if (lessons.isEmpty()) {
            logger.info("No lessons for this course.");
            return false;
        }

        boolean result = false;

        for (Calendar lesson : lessons) {

            if (lesson.getStartDate().isBefore(LocalDateTime.now())) {
                logger.info("Lesson id: {} is finished.", lesson.getId());

                boolean isStudentPresent = lesson.getAttendanceList().stream()
                        .anyMatch(s -> s.getStudentId().equals(studentId) && s.isPresent());

                if (isStudentPresent) {
                    logger.warn("Can't remove a student from this lesson. Because Student is present on this lesson.", lesson.getId());
                    result = true;
                } else {
                    lesson.getAttendanceList().removeIf(s -> s.getStudentId().equals(studentId));
                    calendarRepository.save(lesson);
                }
            } else if (lesson.getStartDate().isAfter(LocalDateTime.now())) {
                logger.info("Lesson id: {}.", lesson.getId());
                logger.info("Remove student from this lesson.", lesson.getId());
                lesson.getAttendanceList().removeIf(s -> s.getStudentId().equals(studentId));
                calendarRepository.save(lesson);
            }
        }
        return result;
    }

    @Override
    public void enrollStudentLesson(String lessonId, Long studentId) {
        logger.info("Trying enrollStudentLesson lessonId: {}, studentId: {}", lessonId, studentId);
        Calendar lessonFromDb = getLessonById(lessonId);

        if (lessonFromDb.getAttendanceList().stream().anyMatch(s -> s.getStudentId().equals(studentId))) {
            logger.warn("Student is already enrolled in the lesson");
            throw new CalendarException(CalendarError.STUDENT_ALREADY_ENROLLED);
        }

        lessonFromDb.getAttendanceList().add(new AttendanceList(studentId));
        calendarRepository.save(lessonFromDb);
    }

    @Override
    public void unEnrollStudentLesson(String lessonId, Long studentId) {
        logger.info("Trying unEnrollStudentLesson lessonId: {}, studentId: {}", lessonId, studentId);
        Calendar lessonFromDb = getLessonById(lessonId);
        lessonFromDb.getAttendanceList().removeIf(s -> s.getStudentId().equals(studentId));
        calendarRepository.save(lessonFromDb);
    }

    @Override
    public void deactivateStudent(Long studentId) {
        List<Calendar> lessons = getLessonsByStudentId(studentId);
        lessons.forEach(lesson -> {
            lesson.getAttendanceList().removeIf(attendance -> attendance.getStudentId().equals(studentId) && !attendance.isPresent());
            calendarRepository.save(lesson);
        });
    }

    @Override
    public void deleteStudentWithAllLessons(Long studentId) {
        List<Calendar> lessons = getLessonsByStudentId(studentId);
        lessons.forEach(lesson -> {
            lesson.getAttendanceList().removeIf(attendance -> attendance.getStudentId().equals(studentId));
            calendarRepository.save(lesson);
        });
    }

    @Override
    public boolean areLessonsWithinNewCourseDates(CourseDto course) {
        logger.info("Are Lessons With in New Course Dates");
        List<Calendar> lessons = getLessonsByCourseId(course.getId());
        LocalDateTime courseStartDate = course.getStartDate();
        LocalDateTime courseEndDate = course.getEndDate();

        for (Calendar lesson : lessons) {
            isLessonStartDateBeforeCourseStartDate(lesson.getStartDate(), courseStartDate);
            isLessonStartDateAfterCourseEndDate(lesson.getStartDate(), courseEndDate);
        }

        return true;
    }


    private Calendar updateLessonStatus(Calendar lesson) {
        logger.info("Updating lesson status.");

        if (lesson.getStartDate().isAfter(LocalDateTime.now())) {
            logger.info("Changing lesson status: {}", Status.INACTIVE);
            lesson.setStatus(Status.INACTIVE);
        }

        if (lesson.getStartDate().isBefore(LocalDateTime.now()) && lesson.getEndDate().isAfter(LocalDateTime.now())) {
            logger.info("Changing lesson status: {}", Status.ACTIVE);
            lesson.setStatus(Status.ACTIVE);
        }

        if (lesson.getEndDate().isBefore(LocalDateTime.now())) {
            logger.info("Changing lesson status: {}", Status.FINISHED);
            lesson.setStatus(Status.FINISHED);
        }

        calendarRepository.save(lesson);
        return lesson;
    }

    private void isLessonStartDateIsAfterLessonEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Checking isLessonStartDateIsAfterLessonEndDate");
        if (startDate.isAfter(endDate)) {
            logger.info("Lesson start date is after lesson end date.");
            throw new CalendarException(CalendarError.LESSON_START_DATE_IS_AFTER_END_DATE);
        }
    }

    private Calendar createSingleLesson(Calendar calendar) {
        logger.info("Creating single lesson.");
        isTeacherActive(calendar.getTeacherId());
        if (isTeacherHaveLessons(calendar.getTeacherId())) {
            List<Calendar> lessons = getLessonsByTeacherId(calendar.getTeacherId());
            isTeacherAvailableOnTimeSlot(lessons, calendar.getStartDate(), calendar.getEndDate());
        }
        return calendar;
    }

    private Calendar createCourseLesson(Calendar calendar) {
        logger.info("Creating course lesson");

        CourseDto courseFromDb = getCourse(calendar.getCourseId(), null);

        if (isLessonExistForThisCourseInCalendar(calendar.getCourseId())) {
            isLessonLimitReached(courseFromDb.getLessonsLimit(), calendar.getCourseId());
            isCourseHaveLessonAvailableOnTimeSlot(calendar.getCourseId(), calendar.getStartDate(), calendar.getEndDate());
        }

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
        return calendarRepository.save(calendar);
    }

    private boolean isTeacherHaveLessons(Long teacherId) {
        logger.info("Checking isTeacherHaveLessons");
        List<Calendar> lessons = getLessonsByTeacherId(teacherId);
        if (lessons.isEmpty()) {
            logger.info("Teacher don't have lessons.");
            return false;
        } else {
            logger.info("Teacher have lessons.");
            return true;
        }
    }

    private CourseDto getCourse(String courseId, Status status) {
        logger.info("Fetching course by id from the database.. courseId: {}", courseId);
        return courseServiceClient.getCourseById(courseId, status);
    }

    private boolean isLessonExistForThisCourseInCalendar(String courseId) {
        logger.info("Checking isLessonExistForThisCourseInCalendar");
        if (getLessonsByCourseId(courseId).isEmpty()) {
            logger.info("No lesson exist for this course in calendar");
            return false;
        }
        logger.info("Lesson exist for this course in calendar");
        return true;
    }

    private void isLessonLimitReached(Long lessonsLimit, String courseId) {
        logger.info("Checking isLessonLimitReached");
        int lessonCount = getLessonsByCourseId(courseId).size();
        if (lessonsLimit == lessonCount || lessonsLimit < lessonCount) {
            throw new CalendarException(CalendarError.LESSON_LIMIT_REACHED_ERROR_MESSAGE);
        }
    }

    private void isCourseHaveLessonAvailableOnTimeSlot(String courseId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Checking isCourseHaveLessonAvailableOnTimeSlot");
        List<Calendar> lessons = getLessonsByCourseId(courseId);

        for (Calendar lesson : lessons) {
            if (startDate.isBefore(lesson.getEndDate()) && endDate.isAfter(lesson.getStartDate())) {
                logger.info("Course is not available at this time slot. Lesson collision.");
                throw new CalendarException(CalendarError.COURSE_BUSY_AT_TIME_SLOT);
            }
        }
    }

    private void isTeacherAvailableOnTimeSlot(List<Calendar> lessons, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Checking isTeacherAvailableOnTimeSlot");
        for (Calendar lesson : lessons) {
            if (startDate.isBefore(lesson.getEndDate()) && endDate.isAfter(lesson.getStartDate())) {
                logger.info("Teacher is not available at this time slot. Lesson collision.");
                throw new CalendarException(CalendarError.TEACHER_BUSY_AT_TIME_SLOT);
            }
        }
    }

    private void isLessonStartDateBeforeCourseStartDate(LocalDateTime lessonStartDate, LocalDateTime courseStartDate) {
        logger.info("Checking isLessonStartDateBeforeCourseStartDate");
        if (lessonStartDate.isBefore(courseStartDate)) {
            logger.info("Lesson start date is before course start date");
            throw new CalendarException(CalendarError.LESSON_START_DATE_IS_BEFORE_COURSE_START_DATE);
        }
    }

    private void isLessonStartDateAfterCourseEndDate(LocalDateTime lessonStartDate, LocalDateTime courseEndDate) {
        logger.info("Checking isLessonStartDateAfterCourseEndDate");
        if (lessonStartDate.isAfter(courseEndDate)) {
            logger.info("Lesson start date is after course end date");
            throw new CalendarException(CalendarError.LESSON_START_DATE_IS_AFTER_COURSE_END_DATE);
        }
    }

    private void isTeacherEnrolledInCourse(Long teacherId, List<CourseTeachersDto> courseTeachers) {
        logger.info("Checking isTeacherEnrolledInCourse");
        if (!courseTeachers.stream().anyMatch(teacherDto -> teacherDto.getId().equals(teacherId))) {
            logger.info("Teacher is not enrolled in course");
            throw new CalendarException(CalendarError.TEACHER_IS_NOT_ENROLLED_IN_COURSE);
        }
    }

    private void isTeacherActive(Long teacherId) {
        logger.info("Checking isTeacherActive");
        teacherServiceClient.teacherIsActive(teacherId);
    }

    private List<AttendanceList> addStudentsToAttendanceList(List<CourseStudentsDto> courseStudents) {
        logger.info("Create attendance list, addStudentsToAttendanceList()");
        if (courseStudents.isEmpty()) {
            logger.info("Course student list is empty.");
            return new ArrayList<>();
        } else {
            return courseStudents.stream()
                    .filter(student -> !student.getStatus().equals(Status.REMOVED))
                    .map(student -> {
                        AttendanceList attendance = new AttendanceList();
                        attendance.setStudentId(student.getId());
                        attendance.setPresent(false);
                        return attendance;
                    }).collect(Collectors.toList());
        }
    }

    private void isLessonEndDateBeforeCourseStartDate(LocalDateTime lessonEndDate, LocalDateTime courseStartDate) {
        logger.info("Checking isLessonEndDateBeforeCourseStartDate");
        if (lessonEndDate.isBefore(courseStartDate)) {
            logger.info("Lesson end date is before course start date");
            throw new CalendarException(CalendarError.LESSON_END_DATE_IS_BEFORE_COURSE_START_DATE);
        }
    }

    private void isLessonEndDateAfterCourseEndDate(LocalDateTime lessonEndDate, LocalDateTime courseEndDate) {
        logger.info("Checking isLessonEndDateAfterCourseEndDate");
        if (lessonEndDate.isAfter(courseEndDate)) {
            logger.info("Lesson end date is after course end date");
            throw new CalendarException(CalendarError.LESSON_END_DATE_IS_AFTER_COURSE_END_DATE);
        }
    }

    private boolean authenticationContext() {
        boolean result = false;
        Authentication authentication = authenticationContext.getAuthentication();
        boolean isStudent = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_student"));
        if (isStudent) {
            result = true;
        }
        return result;
    }

}
