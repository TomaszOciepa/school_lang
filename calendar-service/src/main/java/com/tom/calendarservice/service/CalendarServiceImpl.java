package com.tom.calendarservice.service;

import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.*;
import com.tom.calendarservice.model.Dto.*;
import com.tom.calendarservice.repo.CalendarRepository;
import com.tom.calendarservice.security.AuthenticationContext;
import com.tom.calendarservice.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
    private final RabbitTemplate rabbitTemplate;

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

        boolean updateCourseDate = false;

        if (lesson.getEventName() != null) {
            logger.info("Changing the event name...");
            lessonFromDB.setEventName(lesson.getEventName());
        }

        if (lesson.getStartDate() != null || lesson.getEndDate() != null) {
            boolean startDateChanged = lesson.getStartDate() != null;
            boolean endDateChanged = lesson.getEndDate() != null;

            List<Calendar> lessonsByTeacherId = getLessonsByTeacherId(lesson.getTeacherId());

            if (startDateChanged) {
                logger.info("Changing the lesson start date ...");
                if (lesson.getEndDate() == null) {
                    lesson.setEndDate(lessonFromDB.getEndDate());
                }

                isLessonStartDateIsAfterLessonEndDate(lesson.getStartDate(), lesson.getEndDate());
                isTeacherAvailableOnTimeSlot(lessonsByTeacherId, lesson.getStartDate(), lesson.getEndDate());
                lessonFromDB.setStartDate(lesson.getStartDate());

                if (lessonFromDB.getCourseId() != null) {
                    updateCourseDate = true;
                }
            }

            if (endDateChanged) {
                logger.info("Changing the lesson end date ...");
                if (lesson.getStartDate() == null) {
                    lesson.setStartDate(lessonFromDB.getStartDate());
                }

                isLessonStartDateIsAfterLessonEndDate(lesson.getStartDate(), lesson.getEndDate());
                isTeacherAvailableOnTimeSlot(lessonsByTeacherId, lesson.getStartDate(), lesson.getEndDate());
                lessonFromDB.setEndDate(lesson.getEndDate());

                if (lessonFromDB.getCourseId() != null) {
                    updateCourseDate = true;
                }
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

        Calendar savedLesson = calendarRepository.save(lessonFromDB);

        if (updateCourseDate) {
            updateCourseDateTime(lessonFromDB.getCourseId());
        }

        return savedLesson;
    }

    private void updateCourseDateTime(String courseId) {
        logger.info("Update Course DateTime for courseId: {}", courseId);
        LocalDateTime earliestLesson = findEarliestLesson(courseId);
        LocalDateTime latestLesson = findLatestLesson(courseId);

        CourseDto objUpdateCourseDate = new CourseDto();
        objUpdateCourseDate.setId(courseId);
        objUpdateCourseDate.setStartDate(earliestLesson);
        objUpdateCourseDate.setEndDate(latestLesson);
        rabbitTemplate.convertAndSend("update-course-date", objUpdateCourseDate);
    }

    @Override
    public void deleteLessonsById(String id) {
        Calendar lessonFromDb = calendarRepository.findById(id)
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_NOT_FOUND));
        logger.info("Delete lesson lessonId: {}", id);
        calendarRepository.deleteById(id);
        if (lessonFromDb.getCourseId() != null) {
            updateCourseDateTime(lessonFromDb.getCourseId());
        }
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
    public CourseDto generateCourseTimetable(LessonScheduleRequest lessonScheduleRequest) {
        logger.info("Trying generateCourseTimetable lessonScheduleRequest: {}", lessonScheduleRequest);
        CourseDto courseFromDb = courseServiceClient.getCourseById(lessonScheduleRequest.getCourseId(), null);
        List<Calendar> lessonsByTeacher = getLessonsByTeacherId(lessonScheduleRequest.getTeacherId());

        return scheduleLessons(lessonScheduleRequest, lessonsByTeacher, courseFromDb, lessonScheduleRequest.getLessonFrequency());

    }

    private LocalDateTime setLessonStartDate(LocalTime timeRangeStart, LocalDateTime courseStartDate) {
        return LocalDateTime.of(courseStartDate.getYear(),
                courseStartDate.getMonth().getValue(),
                courseStartDate.getDayOfMonth(),
                timeRangeStart.getHour(),
                timeRangeStart.getMinute());

    }

    private CourseDto scheduleLessons(LessonScheduleRequest lessonScheduleRequest, List<Calendar> lessonsByTeacher, CourseDto courseFromDb, LessonFrequency frequency) {
        LocalTime timeRangeStart = setTimeRange(lessonScheduleRequest.getTimeRange(), "start");
        LocalTime timeRangeEnd = setTimeRange(lessonScheduleRequest.getTimeRange(), "end");
        LocalDateTime startDate = setLessonStartDate(timeRangeStart, courseFromDb.getStartDate());
        long numberLessonsToCreate = courseFromDb.getLessonsLimit();
        int lessonsPerWeek = determineLessonsPerWeek(frequency);
        LocalDateTime currentDayStart = startDate;

        List<DayOfWeek> preferredDays = setPreferredDays(frequency);
        LocalDateTime firstLessonStartDate = null;
        LocalDateTime lastLessonEndDate = null;


        while (numberLessonsToCreate > 0) {
            int lessonsThisWeek = 0;

            for (DayOfWeek day : preferredDays) {
                if (!frequency.equals(LessonFrequency.DAILY)) {
                    currentDayStart = moveToNextAvailableDay(startDate, day).withHour(timeRangeStart.getHour()).withMinute(timeRangeStart.getMinute());
                }

                if (numberLessonsToCreate == 0) {
                    break;
                }

                while (currentDayStart.toLocalTime().isBefore(timeRangeEnd)) {
                    LocalDateTime endDate = currentDayStart.plusMinutes(lessonScheduleRequest.getLessonDuration());

                    boolean teacherAvailable = isTeacherAvailable(lessonsByTeacher, currentDayStart, endDate);

                    if (teacherAvailable) {
                        Calendar newLesson = generateNewLesson(
                                (int) (courseFromDb.getLessonsLimit() - numberLessonsToCreate + 1),
                                currentDayStart, endDate,
                                lessonScheduleRequest.getTeacherId(),
                                courseFromDb
                        );

                        if ((int) (courseFromDb.getLessonsLimit() - numberLessonsToCreate + 1) == 1) {
                            firstLessonStartDate = currentDayStart;
                        }
                        lastLessonEndDate = calendarRepository.save(newLesson).getEndDate();

                        numberLessonsToCreate--;
                        lessonsThisWeek++;

                        if (frequency.equals(LessonFrequency.DAILY)) {
                            currentDayStart = currentDayStart.plusDays(1).withHour(timeRangeStart.getHour()).withMinute(timeRangeStart.getMinute());
                            break;
                        }

                        if (lessonsThisWeek >= lessonsPerWeek) {
                            startDate = currentDayStart.with(DayOfWeek.MONDAY).plusWeeks(1).withHour(timeRangeStart.getHour()).withMinute(timeRangeStart.getMinute());
                            break;
                        }

                        startDate = moveToNextLesson(currentDayStart, day, preferredDays);
                        break;
                    }

                    currentDayStart = currentDayStart.plusHours(1);

                }

                if (lessonsThisWeek >= lessonsPerWeek) {
                    break;
                }
            }

            if (lessonsThisWeek < lessonsPerWeek) {
                startDate = startDate.with(DayOfWeek.MONDAY).plusWeeks(1).withHour(timeRangeStart.getHour()).withMinute(timeRangeStart.getMinute());
            }
        }
        CourseDto courseObjWithFirstAndLastLessonDate = new CourseDto();
        courseObjWithFirstAndLastLessonDate.setStartDate(firstLessonStartDate);
        courseObjWithFirstAndLastLessonDate.setEndDate(lastLessonEndDate);
        return courseObjWithFirstAndLastLessonDate;
    }

    private List<DayOfWeek> setPreferredDays(LessonFrequency frequency) {

        return switch (frequency) {
            case DAILY ->
                    Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
            case WEEKDAYS_ONLY ->
                    Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
            case FOUR_A_WEEK ->
                    Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.WEDNESDAY);
            case WEEKENDS_ONLY -> Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
            default ->
                    Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY);
        };
    }

    private int determineLessonsPerWeek(LessonFrequency frequency) {
        return switch (frequency) {
            case DAILY -> 7;
            case WEEKDAYS_ONLY -> 5;
            case FOUR_A_WEEK -> 4;
            case THREE_A_WEEK -> 3;
            case TWICE_A_WEEK, WEEKENDS_ONLY -> 2;
            case WEEKLY -> 1;
            default -> throw new IllegalArgumentException("Unsupported frequency: " + frequency);
        };
    }

    private LocalDateTime moveToNextAvailableDay(LocalDateTime currentDate, DayOfWeek targetDay) {
        while (currentDate.getDayOfWeek() != targetDay) {
            currentDate = currentDate.plusDays(1);
        }
        return currentDate;
    }

    private LocalDateTime moveToNextLesson(LocalDateTime currentDate, DayOfWeek currentDay, List<DayOfWeek> preferredDays) {
        int currentIndex = preferredDays.indexOf(currentDay);
        if (currentIndex == -1 || currentIndex + 1 >= preferredDays.size()) {
            return currentDate.plusWeeks(1).with(DayOfWeek.MONDAY);
        }
        return currentDate.with(preferredDays.get(currentIndex + 1));
    }


    private LocalTime setTimeRange(TimeRange timeRange, String range) {
        LocalTime time = null;

        switch (timeRange) {
            case MORNING:
                if (range.equals("start")) {
                    time = LocalTime.of(7, 0);
                }
                if (range.equals("end")) {
                    time = LocalTime.of(12, 0);
                }
                break;
            case AFTERNOON:
                if (range.equals("start")) {
                    time = LocalTime.of(12, 0);
                }
                if (range.equals("end")) {
                    time = LocalTime.of(18, 0);
                }
                break;
            case EVENING:
                if (range.equals("start")) {
                    time = LocalTime.of(18, 0);
                }
                if (range.equals("end")) {
                    time = LocalTime.of(22, 0);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown TimeRange");
        }
        return time;
    }


    private Calendar generateNewLesson(int counter, LocalDateTime startDate, LocalDateTime endDate, Long teacherId, CourseDto courseFromDb) {
        Calendar newLesson = new Calendar();
        newLesson.setEventName("Lekcja " + counter);
        newLesson.setStartDate(startDate);
        newLesson.setEndDate(endDate);
        newLesson.setTeacherId(teacherId);
        newLesson.setCourseId(courseFromDb.getId());
        newLesson.setDescription("Lekcja " + counter + ". Została wygenerowana automatycznie.");
        List<AttendanceList> attendanceLists = addStudentsToAttendanceList(courseFromDb.getCourseStudents());
        newLesson.setAttendanceList(attendanceLists);
        return newLesson;
    }

    private boolean isTeacherAvailable(List<Calendar> lessons, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Checking isTeacherAvailable");
        for (Calendar lesson : lessons) {
            if (startDate.isBefore(lesson.getEndDate()) && endDate.isAfter(lesson.getStartDate())) {
                logger.info("Teacher is not available at this time slot. Lesson collision.");
                return false;
            }
        }
        return true;
    }

    private Calendar updateLessonStatus(Calendar lesson) {

        if (lesson.getStartDate().isAfter(LocalDateTime.now())) {
            lesson.setStatus(Status.INACTIVE);
        }

        if (lesson.getStartDate().isBefore(LocalDateTime.now()) && lesson.getEndDate().isAfter(LocalDateTime.now())) {
            lesson.setStatus(Status.ACTIVE);
        }

        if (lesson.getEndDate().isBefore(LocalDateTime.now())) {
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

        isTeacherEnrolledInCourse(calendar.getTeacherId(), courseFromDb.getCourseTeachers());
        isTeacherActive(calendar.getTeacherId());

        if (isTeacherHaveLessons(calendar.getTeacherId())) {
            List<Calendar> lessons = getLessonsByTeacherId(calendar.getTeacherId());
            isTeacherAvailableOnTimeSlot(lessons, calendar.getStartDate(), calendar.getEndDate());
        }

        List<AttendanceList> attendanceLists = addStudentsToAttendanceList(courseFromDb.getCourseStudents());
        calendar.setAttendanceList(attendanceLists);
        Calendar savedLesson = calendarRepository.save(calendar);

        updateCourseDateTime(savedLesson.getCourseId());
        return savedLesson;
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

    private LocalDateTime findEarliestLesson(String courseId) {
        List<Calendar> lessons = getLessonsByCourseId(courseId);

        return lessons.stream()
                .min(Comparator.comparing(Calendar::getStartDate))
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_LESSONS_NOT_FOUND)).getStartDate();

    }

    private LocalDateTime findLatestLesson(String courseId) {
        List<Calendar> lessons = getLessonsByCourseId(courseId);

        return lessons.stream()
                .max(Comparator.comparing(Calendar::getEndDate))
                .orElseThrow(() -> new CalendarException(CalendarError.CALENDAR_LESSONS_NOT_FOUND)).getEndDate();
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
