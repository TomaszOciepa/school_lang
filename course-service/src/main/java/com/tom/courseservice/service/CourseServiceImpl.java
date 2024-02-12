package com.tom.courseservice.service;

import com.tom.courseservice.exception.CourseError;
import com.tom.courseservice.exception.CourseException;
import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.CourseStudents;
import com.tom.courseservice.model.CourseTeachers;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.model.dto.TeacherDto;
import com.tom.courseservice.repo.CourseRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private static Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final StudentServiceClient studentServiceClient;
    private final TeacherServiceClient teacherServiceClient;
    private final CalendarServiceClient calendarServiceClient;

    @Override
    public List<Course> findAllByStatus(Status status) {
        if (status != null) {
            return courseRepository.findAllByStatus(status);
        }
        return courseRepository.findAll();
    }

    public Course findByIdAndStatus(String id, Status status) {
        if (status != null) {
            return courseRepository.findByIdAndStatus(id, status)
                    .orElseThrow(() -> new CourseException(CourseError.COURSE_IS_NOT_ACTIVE));
        }

        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    @Override
    public Course addCourse(Course course) {
        course.setName(course.getName().trim());
        if (courseRepository.existsByName(course.getName())) {
            throw new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
        }

        isCourseStartDateIsAfterCourseEndDate(course.getStartDate(), course.getEndDate());

        course.setParticipantsNumber(0L);
        course.setFinishedLessons(0L);
        return courseRepository.save(course);
    }

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
                    courseFromDb.setLessonsNumber(course.getLessonsNumber());
                    courseFromDb.setStartDate(course.getStartDate());
                    courseFromDb.setEndDate(course.getEndDate());
                    return courseRepository.save(courseFromDb);
                }).orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    @Override
    public Course patchCourse(String id, Course course) {
        Course courseFromDb = courseRepository.findById(id).orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));

        if(course.getName() != null){
            course.setName(course.getName().trim());
            if (!courseFromDb.getName().equals(course.getName())
                    && courseRepository.existsByName(course.getName())) {
                throw new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
            }
            courseFromDb.setName(course.getName());
        }

        if (course.getStatus() != null){
            if(course.getStatus().equals(Status.ACTIVE)){
                if(courseFromDb.getParticipantsNumber().equals(courseFromDb.getParticipantsLimit())){
                    throw new CourseException(CourseError.COURSE_IS_FULL);
                }
            }

            if(course.getStatus().equals(Status.FULL)){
                if(courseFromDb.getParticipantsNumber() < courseFromDb.getParticipantsLimit()){
                    throw new CourseException(CourseError.COURSE_IS_NOT_FULL);
                }
            }
            courseFromDb.setStatus(course.getStatus());
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

        if (course.getParticipantsNumber() != null) {

            if(courseFromDb.getParticipantsLimit() < course.getParticipantsNumber()){
                throw new CourseException(CourseError.COURSE_PARTICIPANTS_NUMBER_IS_BIGGER_THEN_PARTICIPANTS_LIMIT);
            }
            courseFromDb.setParticipantsNumber(course.getParticipantsNumber());
        }

        if (course.getLessonsNumber() != null) {


            courseFromDb.setLessonsNumber(course.getLessonsNumber());
        }

        if (course.getFinishedLessons() != null) {

            if(courseFromDb.getLessonsNumber() < course.getFinishedLessons()){
                throw new CourseException(CourseError.COURSE_LESSONS_FINISHED_IS_BIGGER_THEN_LESSONS_NUMBER);
            }
            courseFromDb.setFinishedLessons(course.getFinishedLessons());
        }

        if (course.getStartDate() != null) {
            isCourseStartDateIsAfterCourseEndDate(course.getStartDate(), courseFromDb.getEndDate());
            courseFromDb.setStartDate(course.getStartDate());
        }

        if (course.getEndDate() != null) {
            isCourseEndDateIsBeforeCourseStartDate(course.getEndDate(), courseFromDb.getStartDate());
            courseFromDb.setEndDate(course.getEndDate());
        }

        return courseRepository.save(courseFromDb);
    }

    @Override
    public void deleteCourse(String id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
        courseRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> studentCourseEnrollment(String courseId, Long studentId) {
        Course course = findByIdAndStatus(courseId, null);
        if (course.getStatus().equals(Status.FULL)) {
            throw new CourseException(CourseError.COURSE_IS_FULL);
        }
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
        Course courseFromDb = findByIdAndStatus(courseId, null);

        if (!isStudentEnrolledInCourse(courseFromDb, studentId)) {
            throw new CourseException(CourseError.STUDENT_NO_ON_THE_LIST_OF_ENROLL);
        }

        if (courseFromDb.getStartDate().isAfter(LocalDateTime.now())) {
            calendarServiceClient.unEnrollStudent(courseId, studentId);
            removeStudentFromCourseStudentList(studentId, courseFromDb);
        } else {
            if (calendarServiceClient.unEnrollStudent(courseId, studentId)) {
                setRemovedStatus(studentId, courseFromDb);
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
        Course course = findByIdAndStatus(courseId, null);
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
        Course courseFromDb = findByIdAndStatus(courseId, null);
        List<CourseTeachers> courseTeacherList = courseFromDb.getCourseTeachers();
        boolean removed = courseTeacherList.removeIf(teacher -> teacherId.equals(teacher.getTeacherId()));
        if (!removed) {
            throw new CourseException(CourseError.TEACHER_NO_ON_THE_LIST_OF_ENROLL);
        }
        courseFromDb.setCourseTeachers(courseTeacherList);
        courseRepository.save(courseFromDb);
    }

    @Override
    public List<StudentDto> getCourseMembers(String courseId) {
        Course course = findByIdAndStatus(courseId, null);
        List<CourseStudents> courseStudents = course.getCourseStudents();
        if (courseStudents.isEmpty()) {
            throw new CourseException(CourseError.COURSE_STUDENT_LIST_IS_EMPTY);
        }
        List<Long> idNumbers = courseStudents.stream()
                .map(CourseStudents::getStudentId)
                .collect(Collectors.toList());

        return studentServiceClient.getStudentsByIdNumber(idNumbers);
    }

    @Override
    public List<TeacherDto> getCourseTeachers(String courseId) {
        Course course = findByIdAndStatus(courseId, null);
        List<CourseTeachers> courseTeachers = course.getCourseTeachers();
        if (courseTeachers.isEmpty()) {
            throw new CourseException(CourseError.COURSE_TEACHER_LIST_IS_EMPTY);
        }
        List<Long> idNumbers = courseTeachers.stream()
                .map(CourseTeachers::getTeacherId)
                .collect(Collectors.toList());

        return teacherServiceClient.getTeachersByIdNumber(idNumbers);
    }

    @Override
    public void changeCourseMemberStatus(String courseId, Long studentId, Status status) {

        Course courseFromDb = findByIdAndStatus(courseId, null);

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

    private void isCourseStartDateIsAfterCourseEndDate(LocalDateTime startDate, LocalDateTime endTime) {
        if(startDate.isAfter(endTime) || startDate.isEqual(endTime)){
            throw new CourseException(CourseError.COURSE_START_DATE_IS_AFTER_END_DATE);
        }
    }

    private void isCourseEndDateIsBeforeCourseStartDate(LocalDateTime endDate, LocalDateTime startTime) {
        if(endDate.isBefore(startTime) || endDate.isEqual(startTime)){
            throw new CourseException(CourseError.COURSE_END_DATE_IS_BEFORE_START_DATE);
        }
    }

}
