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
        logger.info("Create students id numbers list.");
        List<Long> idNumbers = courseStudents.stream()
                .map(CourseStudents::getStudentId)
                .collect(Collectors.toList());


        logger.info("Fetching students by id number.");
        List<StudentDto> studentsFromDb = studentServiceClient.getStudentsByIdNumbers(idNumbers);

        List<CourseStudentDto> courseStudentList = createCourseStudentList(courseStudents, studentsFromDb);

        return courseStudentList;
    }

    @Override
    public List<TeacherDto> getCourseTeachers(String courseId) {
        logger.info("Fetching courses by id: {}.", courseId);
        Course course = getCourseById(courseId, null);
        logger.info("Fetching course teachers.");
        List<CourseTeachers> courseTeachers = course.getCourseTeachers();
        if (courseTeachers.isEmpty()) {
            throw new CourseException(CourseError.COURSE_TEACHER_LIST_IS_EMPTY);
        }
        logger.info("Create teachers id numbers list.");
        List<Long> idNumbers = courseTeachers.stream()
                .map(CourseTeachers::getTeacherId)
                .collect(Collectors.toList());

        logger.info("Fetching teachers by id number.");
        return teacherServiceClient.getTeachersByIdNumber(idNumbers);
    }

    @Override
    public Course patchCourse(String id, Course course) {
        logger.info("Fetching course by ID from the database: {}", id);
        Course courseFromDb = courseRepository.findById(id).orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));

        if (course.getName() != null) {
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
            logger.info("Changing the course participants limit...");
            if (course.getParticipantsLimit() < courseFromDb.getParticipantsNumber()) {
                throw new CourseException(CourseError.COURSE_PARTICIPANTS_NUMBER_IS_BIGGER_THEN_PARTICIPANTS_LIMIT);
            }

            if (course.getParticipantsLimit() == courseFromDb.getParticipantsNumber()) {
                courseFromDb.setStatus(Status.FULL);
            }

            if (course.getParticipantsLimit() > courseFromDb.getParticipantsNumber()) {
                courseFromDb.setStatus(Status.ACTIVE);
            }
            courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
        }

        if (course.getLessonsLimit() != null) {
            logger.info("Changing the course lessons limit...");
            if (course.getLessonsLimit() < calendarServiceClient.getLessonsNumberByCourseId(courseFromDb.getId())) {
                throw new CourseException(CourseError.COURSE_LESSONS_NUMBER_IS_BIGGER_THEN_LESSONS_LIMIT);
            }
            courseFromDb.setLessonsLimit(course.getLessonsLimit());
        }

        if (course.getStartDate() != null) {
            logger.info("Changing the course start date ...");
            isCourseStartDateIsAfterCourseEndDate(course.getStartDate(), course.getEndDate());
            courseFromDb.setStartDate(course.getStartDate());
        }

        if (course.getEndDate() != null) {
            logger.info("Changing the course end date ...");
            LocalDateTime endDate = course.getEndDate();
            course.setEndDate(endDate.plusHours(23).plusMinutes(59));

            isCourseEndDateIsBeforeCourseStartDate(course.getEndDate(), course.getStartDate());
            courseFromDb.setEndDate(course.getEndDate());
        }

        return courseRepository.save(courseFromDb);
    }

    @Override
    public void deleteCourseById(String id) {
        logger.info("Trying delete course by id: {}", id);
        logger.info("Fetching course, if exists");
        courseRepository.findById(id)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
        logger.info("Delete course.");
        courseRepository.deleteById(id);
        logger.info("Delete course lessons.");
        calendarServiceClient.deleteCourseLessons(id);
    }

    @Override
    public void assignTeacherToCourse(String courseId, Long teacherId) {
        logger.info("Trying assignTeacherToCourse, courseId: {}, teacherId: {}", courseId, teacherId);
        Course course = getCourseById(courseId, null);

        TeacherDto teacherDto = teacherServiceClient.getTeacherById(teacherId);
        validateTeacherBeforeCourseEnrollment(course, teacherDto);
        CourseTeachers courseTeachers = new CourseTeachers(teacherDto.getId());
        course.getCourseTeachers().add(courseTeachers);
        logger.info("Assign teacher to course is done.");
        courseRepository.save(course);
    }

    @Override
    public void teacherCourseUnEnrollment(String courseId, Long teacherId) {
        logger.info("teacherCourseUnEnrollment courseId: {}, teacherId: {}", courseId, teacherId);
        Course courseFromDb = getCourseById(courseId, null);
        logger.info("Fetching course list done.");

        if (!courseFromDb.getCourseTeachers().stream().anyMatch(t -> t.getTeacherId().equals(teacherId))) {
            logger.info("No teacher on the list of enroll");
            throw new CourseException(CourseError.TEACHER_NO_ON_THE_LIST_OF_ENROLL);
        }


        if (calendarServiceClient.isTeacherAssignedToLessonInCourse(courseId, teacherId)) {
            logger.info("Teacher has lessons in course");
            throw new CourseException(CourseError.TEACHER_HAS_LESSONS_IN_COURSE);
        }

        logger.info("Removing teachers from course.");
        List<CourseTeachers> courseTeacherList = courseFromDb.getCourseTeachers();
        boolean removed = courseTeacherList.removeIf(teacher -> teacherId.equals(teacher.getTeacherId()));
        if (!removed) {
            throw new CourseException(CourseError.TEACHER_NO_ON_THE_LIST_OF_ENROLL);
        }
        courseFromDb.setCourseTeachers(courseTeacherList);
        courseRepository.save(courseFromDb);
    }

    @Override
    public ResponseEntity<?> assignStudentToCourse(String courseId, Long studentId) {
        logger.info("Trying assignStudentToCourse courseId: {}, studentId: {}", courseId, studentId);
        Course course = getCourseById(courseId, null);

        if (course.getParticipantsLimit().equals(course.getParticipantsNumber())) {
            throw new CourseException(CourseError.COURSE_IS_FULL);
        }

        StudentDto studentDto = studentServiceClient.getStudentById(studentId);

        if (isStudentEnrolledInCourse(course, studentDto.getId())) {
            logger.info("Student already enrolled on this course");
            throw new CourseException(CourseError.STUDENT_ALREADY_ENROLLED);
        }

        course.getCourseStudents().add(new CourseStudents(studentDto.getId(), Status.ACTIVE));
        course.incrementParticipantsNumber();
        courseRepository.save(course);
        enrollStudentToLessons(course.getId(), studentDto.getId());
        return ResponseEntity.ok().build();
    }

    @Override
    public void studentCourseUnEnrollment(String courseId, Long studentId) {
        logger.info("studentCourseUnEnrollment courseId: {}, studentId: {}", courseId, studentId);
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

    public ResponseEntity<?> restoreStudentToCourse(String courseId, Long studentId) {
        logger.info("restoreStudentToCourse courseId: {}, studentId: {}", courseId, studentId);
        Course course = getCourseById(courseId, null);
        if (course.getParticipantsLimit().equals(course.getParticipantsNumber())) {
            logger.warn("Course is full.");
            throw new CourseException(CourseError.COURSE_IS_FULL);
        }

        if (!isStudentEnrolledInCourse(course, studentId)) {
            logger.warn("No student on the list of enroll.");
            throw new CourseException(CourseError.STUDENT_NO_ON_THE_LIST_OF_ENROLL);
        }

        course.getCourseStudents().forEach(student -> {
            if (student.getStudentId().equals(studentId) && student.getStatus().equals(Status.ACTIVE)) {
                logger.warn("Student is ACTIVE.");
                throw new CourseException(CourseError.STUDENT_IS_ACTIVE);
            }

            if (student.getStudentId().equals(studentId)) {
                logger.info("Student with studentId: {} set status Active.", studentId);
                student.setStatus(Status.ACTIVE);
            }
        });

        course.incrementParticipantsNumber();
        courseRepository.save(course);
        enrollStudentToLessons(course.getId(), studentId);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<Course> getCourseByTeacherId(Long teacherId) {
        logger.info("getCourseByTeacherId: teacherId: {}", teacherId);
        List<Course> courses = courseRepository.getCoursesByTeacherId(teacherId);
        if (courses.isEmpty()) {
            logger.info("Courses list is empty.");
            throw new CourseException(CourseError.COURSE_NOT_FOUND);
        }
        return courses;
    }

    @Override
    public List<Course> getCourseByStudentId(Long studentId) {
        logger.info("getCourseByStudentId: studentId: {}", studentId);
        List<Course> courses = courseRepository.getCoursesByStudentId(studentId);
        if (courses.isEmpty()) {
            logger.info("Courses list is empty.");
            throw new CourseException(CourseError.COURSE_NOT_FOUND);
        }
        return courses;
    }

    private void isCourseStartDateIsAfterCourseEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Checking isCourseStartDateIsAfterCourseEndDate");
        logger.info("Start date is: {}, end date is: {}.", startDate, endDate);
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new CourseException(CourseError.COURSE_START_DATE_IS_AFTER_END_DATE);
        }
    }

    private void isCourseEndDateIsBeforeCourseStartDate(LocalDateTime endDate, LocalDateTime startTime) {
        logger.info("Checking isCourseEndDateIsBeforeCourseStartDate");
        logger.info("Start date is: {}, end date is: {}.", startTime, endDate);
        if (endDate.isBefore(startTime) || endDate.isEqual(startTime)) {
            throw new CourseException(CourseError.COURSE_END_DATE_IS_BEFORE_START_DATE);
        }
    }

    private Course updateCourseStatus(Course course) {
        logger.info("Updating course status");
        if (course.getStartDate().isAfter(LocalDateTime.now())) {
            course.setStatus(Status.INACTIVE);
            logger.info("Set status: {}", Status.INACTIVE);
        }

        if (course.getStartDate().isBefore(LocalDateTime.now()) && course.getEndDate().isAfter(LocalDateTime.now())) {
            course.setStatus(Status.ACTIVE);
            logger.info("Set status: {}.", Status.ACTIVE);
        }

        if (course.getEndDate().isBefore(LocalDateTime.now())) {
            course.setStatus(Status.FINISHED);
            logger.info("Set status: {}.", Status.FINISHED);
        }

        return courseRepository.save(course);
    }

    private List<CourseStudentDto> createCourseStudentList(List<CourseStudents> courseStudents, List<StudentDto> studentsFromDb) {
        logger.info("Creating course student list.");
        List<CourseStudentDto> courseStudentList = new ArrayList<>();

        for (StudentDto student : studentsFromDb) {

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

    private void validateTeacherBeforeCourseEnrollment(Course course, TeacherDto teacherDto) {
        logger.info("validateTeacherBeforeCourseEnrollment");
        if (!Status.ACTIVE.equals(teacherDto.getStatus())) {
            logger.warn("Teacher is not active.");
            throw new CourseException(CourseError.TEACHER_IS_NOT_ACTIVE);
        }
        if (course.getCourseTeachers()
                .stream()
                .anyMatch((member -> teacherDto.getId().equals(member.getTeacherId())))) {
            logger.warn("Teacher is already enrolled.");
            throw new CourseException(CourseError.TEACHER_ALREADY_ENROLLED);
        }
    }

    private boolean isStudentEnrolledInCourse(Course course, Long studentId) {
        logger.info("Checking isStudentEnrolledInCourse");
        boolean match = course.getCourseStudents()
                .stream()
                .anyMatch((member -> studentId.equals(member.getStudentId())));
        if (match) {
            return true;
        } else {
            return false;
        }
    }

    private ResponseEntity<?> enrollStudentToLessons(String curseId, Long studentId) {
        logger.info("Trying enrollStudentToLessons");
        calendarServiceClient.enrollStudent(curseId, studentId);
        return ResponseEntity.ok().build();
    }

    private void removeStudentFromCourseStudentList(Long studentId, Course courseFromDb) {
        logger.info("removeStudentFromCourseStudentList studentId: {}, courseName: {}", studentId, courseFromDb.getName());
        List<CourseStudents> courseStudentsList = courseFromDb.getCourseStudents();

        boolean removed = courseStudentsList.removeIf(student -> studentId.equals(student.getStudentId()));

        if (!removed) {
            logger.warn("No student on the list of enroll");
            throw new CourseException(CourseError.STUDENT_NO_ON_THE_LIST_OF_ENROLL);
        }
        courseFromDb.setCourseStudents(courseStudentsList);
        courseFromDb.decrementParticipantsNumber();
    }

    private void setRemovedStatus(Long studentId, Course courseFromDb) {
        logger.info("setRemovedStatus for studentId: {} in courseName: {}", studentId, courseFromDb.getName());
        courseFromDb.getCourseStudents().stream().map(student -> {
            if (student.getStudentId().equals(studentId)) {
                student.setStatus(Status.REMOVED);
            }
            return student;
        }).collect(Collectors.toList());
        courseRepository.save(courseFromDb);
    }
//    nie sprawdzone

//    private void validateCourseStatus(Course course) {
//        if (!Status.ACTIVE.equals(course.getStatus())) {
//            throw new CourseException(CourseError.COURSE_IS_NOT_ACTIVE);
//        }
//    }
//
//    @Override
//    public void changeCourseMemberStatus(String courseId, Long studentId, Status status) {
//
//        Course courseFromDb = getCourseById(courseId, null);
//
//        courseFromDb.getCourseStudents().stream().map(student -> {
//            if (student.getStudentId().equals(studentId)) {
//                student.setStatus(status);
//            }
//            return student;
//        }).collect(Collectors.toList());
//        courseRepository.save(courseFromDb);
//
//        if (status.equals(Status.ACTIVE)) {
//            enrollStudentToLessons(courseId, studentId);
//        }
//
//    }


}
