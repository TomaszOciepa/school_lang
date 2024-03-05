package com.tom.courseservice.service;

import com.tom.courseservice.exception.CourseError;
import com.tom.courseservice.exception.CourseException;
import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.CourseStudents;
import com.tom.courseservice.model.CourseTeachers;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.CourseStudentDto;
import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.model.dto.TeacherDto;
import com.tom.courseservice.repo.CourseRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private static Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final StudentServiceClient studentServiceClient;
    private final TeacherServiceClient teacherServiceClient;
    private final CalendarServiceClient calendarServiceClient;

    //sprawdzone

    @Override
    public Course addCourse(Course course) {
        logger.info("Adding new course: {}", course);
        logger.info("Removing white spaces from the name.");
        course.setName(course.getName().trim());

        logger.info("Checking if the name already exists.");
        if (courseRepository.existsByName(course.getName())) {
            throw new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
        }

        logger.info("Setting the end date of the course.");
        LocalDateTime endDate = course.getEndDate();
        course.setEndDate(endDate.plusHours(23).plusMinutes(59));

        logger.info("Checking if start date is after end date.");
        isCourseStartDateIsAfterCourseEndDate(course.getStartDate(), course.getEndDate());

        logger.info("Setting participants number on 0L.");
        course.setParticipantsNumber(0L);
        logger.info("Save course on database.");
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllByStatus(Status status) {
        logger.info("getAllByStatus()");
        if (status != null) {
            logger.info("Fetching courses with status: {}.", status);
            return courseRepository.getAllByStatus(status).stream()
                    .map(this::updateCourseStatus)
                    .collect(Collectors.toList());
        }
        logger.info("Fetching courses without status: {}.", status);
        return courseRepository.findAll().stream()
                .map(this::updateCourseStatus)
                .collect(Collectors.toList());
    }
    @Override
    public Course getCourseById(String id, Status status) {
        if (status != null) {
            logger.info("Fetching courses with status: {}.", status);
            logger.info("");
            return courseRepository.findByIdAndStatus(id, status)
                    .map(this::updateCourseStatus)
                    .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
        }

        logger.info("Fetching courses without status: {}.", status);
        return courseRepository.findById(id)
                .map(this::updateCourseStatus)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    @Override
    public List<CourseStudentDto> getCourseMembers(String courseId) {
        logger.info("Fetching courses by id: {}.", courseId);
        Course course = getCourseById(courseId, null);
        logger.info("Fetching course members.");
        List<CourseStudents> courseStudents = course.getCourseStudents();
        if (courseStudents.isEmpty()) {
            logger.info("Student list is empty.");
            throw new CourseException(CourseError.COURSE_STUDENT_LIST_IS_EMPTY);
        }
        List<Long> idNumbers = courseStudents.stream()
                .map(CourseStudents::getStudentId)
                .collect(Collectors.toList());
        logger.info("Create student id numbers list.");

        logger.info("Fetching students by id number.");
        List<StudentDto> studentsFromDb = studentServiceClient.getStudentsByIdNumbers(idNumbers);

        List<CourseStudentDto> courseStudentList = createCourseStudentList(courseStudents, studentsFromDb);

        return courseStudentList;
    }

    @Override
    public List<TeacherDto> getCourseTeachers(String courseId) {
        logger.info("Fetching course by id number.");
        Course course = getCourseById(courseId, null);

        List<CourseTeachers> courseTeachers = course.getCourseTeachers();
        if (courseTeachers.isEmpty()) {
            throw new CourseException(CourseError.COURSE_TEACHER_LIST_IS_EMPTY);
        }
        List<Long> idNumbers = courseTeachers.stream()
                .map(CourseTeachers::getTeacherId)
                .collect(Collectors.toList());

        return teacherServiceClient.getTeachersByIdNumber(idNumbers);
    }

    private void isCourseStartDateIsAfterCourseEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Checking isCourseStartDateIsAfterCourseEndDate");
        logger.info("Start date is: {}, end date is: {}.", startDate, endDate);
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)){
            throw new CourseException(CourseError.COURSE_START_DATE_IS_AFTER_END_DATE);
        }
    }

    private void isCourseEndDateIsBeforeCourseStartDate(LocalDateTime endDate, LocalDateTime startTime) {
        logger.info("Checking isCourseEndDateIsBeforeCourseStartDate");
        logger.info("Start date is: {}, end date is: {}.", startTime, endDate);
        if(endDate.isBefore(startTime) || endDate.isEqual(startTime)){
            throw new CourseException(CourseError.COURSE_END_DATE_IS_BEFORE_START_DATE);
        }
    }

    private Course updateCourseStatus(Course course){
        logger.info("Updating course status");
        if(course.getStartDate().isAfter(LocalDateTime.now())){
            course.setStatus(Status.INACTIVE);
            logger.info("Set status: {}", Status.INACTIVE);
        }

        if(course.getStartDate().isBefore(LocalDateTime.now()) && course.getEndDate().isAfter(LocalDateTime.now())){
            course.setStatus(Status.ACTIVE);
            logger.info("Set status: {}.", Status.ACTIVE);
        }



        if(course.getEndDate().isBefore(LocalDateTime.now())){
            course.setStatus(Status.FINISHED);
            logger.info("Set status: {}.", Status.FINISHED);
        }

        return courseRepository.save(course);
    }

    private List<CourseStudentDto> createCourseStudentList(List<CourseStudents> courseStudents, List<StudentDto> studentsFromDb) {
        logger.info("Creating course student list.");
        List<CourseStudentDto> courseStudentList = new ArrayList<>();

        for (StudentDto student: studentsFromDb){

            Long id = student.getId();
            String firstName = student.getFirstName();
            String lastName = student.getLastName();

            Optional<CourseStudents> first = courseStudents.stream().filter(s -> s.getStudentId().equals(id)).findFirst();
            LocalDateTime enrollmentDate = first.get().getEnrollmentDate();
            Status status = first.get().getStatus();

            courseStudentList.add(new CourseStudentDto(id, firstName, lastName, enrollmentDate, status));

        }
        return courseStudentList;
    }


//    nie sprawdzone


    @Override
    public Course putCourse(String id, Course course) {
        return courseRepository.findById(id)
                .map(courseFromDb -> {
                    if (!courseFromDb.getName().equals(course.getName())
                            && courseRepository.existsByName(course.getName())) {
                        throw new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
                    }
                    courseFromDb.setName(course.getName());
                    courseFromDb.setStatus(course.getStatus());
                    courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
                    courseFromDb.setParticipantsNumber(course.getParticipantsNumber());
                    courseFromDb.setLessonsLimit(course.getLessonsLimit());
                    courseFromDb.setStartDate(course.getStartDate());
                    courseFromDb.setEndDate(course.getEndDate());
                    return courseRepository.save(courseFromDb);
                }).orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    @Override
    public Course patchCourse(String id, Course course) {
        logger.info("Fetching course by ID from the database: {}", id);
        Course courseFromDb = courseRepository.findById(id).orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));

        if(course.getName() != null){
            logger.info("Changing the course name...");
            course.setName(course.getName().trim());
            if (!courseFromDb.getName().equals(course.getName())
                    && courseRepository.existsByName(course.getName())) {
                logger.info("Course name is already exists.");
                throw new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
            }
            courseFromDb.setName(course.getName());
        }

        if (course.getParticipantsLimit() != null) {
            if(course.getParticipantsLimit() < courseFromDb.getParticipantsNumber() ){
                throw new CourseException(CourseError.COURSE_PARTICIPANTS_NUMBER_IS_BIGGER_THEN_PARTICIPANTS_LIMIT);
            }

            if(course.getParticipantsLimit() == courseFromDb.getParticipantsNumber()){
                courseFromDb.setStatus(Status.FULL);
            }

            if(course.getParticipantsLimit() > courseFromDb.getParticipantsNumber()){
                courseFromDb.setStatus(Status.ACTIVE);
            }
            courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
        }

        if (course.getLessonsLimit() != null) {

            if(course.getLessonsLimit() < calendarServiceClient.getLessonsNumberByCourseId(courseFromDb.getId())){
                throw new CourseException(CourseError.COURSE_LESSONS_NUMBER_IS_BIGGER_THEN_LESSONS_LIMIT);
            }
            courseFromDb.setLessonsLimit(course.getLessonsLimit());
        }

        if (course.getStartDate() != null) {
            isCourseStartDateIsAfterCourseEndDate(course.getStartDate(), course.getEndDate());
            courseFromDb.setStartDate(course.getStartDate());
        }

        if (course.getEndDate() != null) {
            LocalDateTime endDate = course.getEndDate();
            course.setEndDate(endDate.plusHours(23).plusMinutes(59));

            isCourseEndDateIsBeforeCourseStartDate(course.getEndDate(), course.getStartDate());
            courseFromDb.setEndDate(course.getEndDate());
        }

        return courseRepository.save(courseFromDb);
    }

    @Override
    public void deleteCourse(String id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
        courseRepository.deleteById(id);
        calendarServiceClient.deleteCourseLessons(id);
    }

    @Override
    public ResponseEntity<?> studentCourseEnrollment(String courseId, Long studentId) {
        Course course = getCourseById(courseId, null);

        if(course.getParticipantsLimit().equals(course.getParticipantsNumber())){
            throw new CourseException(CourseError.COURSE_IS_FULL);
        }
//        if (course.getStatus().equals(Status.FULL)) {
//            throw new CourseException(CourseError.COURSE_IS_FULL);
//        }

//        validateCourseStatus(course);
        StudentDto studentDto = studentServiceClient.getStudentById(studentId);

        if (isStudentEnrolledInCourse(course, studentDto.getId())) {
            throw new CourseException(CourseError.STUDENT_ALREADY_ENROLLED);
        }

        course.getCourseStudents().add(new CourseStudents(studentDto.getId(), Status.ACTIVE));
        course.incrementParticipantsNumber();
        courseRepository.save(course);
        enrollStudentToLessons(course.getId(), studentDto.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> restoreStudentToCourse(String courseId, Long studentId){
        Course course = getCourseById(courseId, null);

        if(course.getParticipantsLimit().equals(course.getParticipantsNumber())){
            throw new CourseException(CourseError.COURSE_IS_FULL);
        }

        if(!isStudentEnrolledInCourse(course, studentId)){
            throw new CourseException(CourseError.STUDENT_NO_ON_THE_LIST_OF_ENROLL);
        }

        course.getCourseStudents().forEach(student ->{
            if(student.getStudentId().equals(studentId) && student.getStatus().equals(Status.ACTIVE)){
                throw new CourseException(CourseError.STUDENT_IS_ACTIVE);
            }

            if(student.getStudentId().equals(studentId)){
                student.setStatus(Status.ACTIVE);
            }
        });

        course.incrementParticipantsNumber();
        courseRepository.save(course);
        enrollStudentToLessons(course.getId(), studentId);
        return ResponseEntity.ok().build();
    }
    private ResponseEntity<?> enrollStudentToLessons(String curseId, Long studentId) {
        calendarServiceClient.enrollStudent(curseId, studentId);
        return ResponseEntity.ok().build();
    }

    private boolean isStudentEnrolledInCourse(Course course, Long studentId) {
        boolean match = course.getCourseStudents()
                .stream()
                .anyMatch((member -> studentId.equals(member.getStudentId())));

        if (match) {
            return true;
        } else {
            return false;
        }
    }


    private void validateCourseStatus(Course course) {
        if (!Status.ACTIVE.equals(course.getStatus())) {
            throw new CourseException(CourseError.COURSE_IS_NOT_ACTIVE);
        }
    }

    @Override
    public void studentRemoveFromCourse(String courseId, Long studentId) {
        Course courseFromDb = getCourseById(courseId, null);

        if (!isStudentEnrolledInCourse(courseFromDb, studentId)) {
            throw new CourseException(CourseError.STUDENT_NO_ON_THE_LIST_OF_ENROLL);
        }

        if (courseFromDb.getStartDate().isAfter(LocalDateTime.now())) {
            calendarServiceClient.unEnrollStudent(courseId, studentId);
            removeStudentFromCourseStudentList(studentId, courseFromDb);
        } else {
            if (calendarServiceClient.unEnrollStudent(courseId, studentId)) {
                setRemovedStatus(studentId, courseFromDb);
                courseFromDb.decrementParticipantsNumber();
            } else {
                removeStudentFromCourseStudentList(studentId, courseFromDb);
            }
        }

        courseRepository.save(courseFromDb);
    }

    private void setRemovedStatus(Long studentId, Course courseFromDb) {
        courseFromDb.getCourseStudents().stream().map(student -> {
            if (student.getStudentId().equals(studentId)) {
                student.setStatus(Status.REMOVED);
            }
            return student;
        }).collect(Collectors.toList());

        courseRepository.save(courseFromDb);
    }

    private void removeStudentFromCourseStudentList(Long studentId, Course courseFromDb) {
        List<CourseStudents> courseStudentsList = courseFromDb.getCourseStudents();

        boolean removed = courseStudentsList.removeIf(student -> studentId.equals(student.getStudentId()));

        if (!removed) {
            throw new CourseException(CourseError.STUDENT_NO_ON_THE_LIST_OF_ENROLL);
        }
        courseFromDb.setCourseStudents(courseStudentsList);
        courseFromDb.decrementParticipantsNumber();
    }

    @Override
    public void teacherCourseEnrollment(String courseId, Long teacherId) {
        Course course = getCourseById(courseId, null);
//        validateCourseStatus(course);
        TeacherDto teacherDto = teacherServiceClient.getTeacherById(teacherId);
        validateTeacherBeforeCourseEnrollment(course, teacherDto);
        CourseTeachers courseTeachers = new CourseTeachers(teacherDto.getId());
        course.getCourseTeachers().add(courseTeachers);
        courseRepository.save(course);
    }

    private void validateTeacherBeforeCourseEnrollment(Course course, TeacherDto teacherDto) {
        if (!Status.ACTIVE.equals(teacherDto.getStatus())) {
            throw new CourseException(CourseError.TEACHER_IS_NOT_ACTIVE);
        }
        if (course.getCourseTeachers()
                .stream()
                .anyMatch((member -> teacherDto.getId().equals(member.getTeacherId())))) {
            throw new CourseException(CourseError.TEACHER_ALREADY_ENROLLED);
        }
    }

    @Override
    public void teacherRemoveFromCourse(String courseId, Long teacherId) {
        Course courseFromDb = getCourseById(courseId, null);

        if(!courseFromDb.getCourseTeachers().stream().anyMatch(t-> t.getTeacherId().equals(teacherId))){
            throw new CourseException(CourseError.TEACHER_NO_ON_THE_LIST_OF_ENROLL);
        }

        if(calendarServiceClient.isTeacherAssignedToLessonInCourse(courseId, teacherId)){
            throw new CourseException(CourseError.TEACHER_HAS_LESSONS_IN_COURSE);
        }


        List<CourseTeachers> courseTeacherList = courseFromDb.getCourseTeachers();
        boolean removed = courseTeacherList.removeIf(teacher -> teacherId.equals(teacher.getTeacherId()));
        if (!removed) {
            throw new CourseException(CourseError.TEACHER_NO_ON_THE_LIST_OF_ENROLL);
        }
        courseFromDb.setCourseTeachers(courseTeacherList);
        courseRepository.save(courseFromDb);


    }

    @Override
    public void changeCourseMemberStatus(String courseId, Long studentId, Status status) {

        Course courseFromDb = getCourseById(courseId, null);

        courseFromDb.getCourseStudents().stream().map(student -> {
            if (student.getStudentId().equals(studentId)) {
                student.setStatus(status);
            }
            return student;
        }).collect(Collectors.toList());
        courseRepository.save(courseFromDb);

        if (status.equals(Status.ACTIVE)) {
            enrollStudentToLessons(courseId, studentId);
        }

    }





}
