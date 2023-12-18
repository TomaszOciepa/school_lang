package com.tom.courseservice.service;

import com.tom.courseservice.exception.CourseError;
import com.tom.courseservice.exception.CourseException;
import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.CourseStudents;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.repo.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentServiceClient studentServiceClient;

    @Override
    public List<Course> findAllByStatus(Status status) {
        if (status != null) {
            return courseRepository.findAllByStatus(status);
        }
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(String id) {

        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    @Override
    public Course addCourse(Course course) {
        if (courseRepository.existsByName(course.getName())) {
            throw new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
        }
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
        return courseRepository.findById(id)
                .map(courseFromDb -> {
                    if (!course.getName().isEmpty()) {
                        courseFromDb.setName(course.getName());
                    }
                    if (course.getStatus() != null) {
                        courseFromDb.setStatus(course.getStatus());
                    }
                    if (course.getParticipantsNumber() != null) {
                        courseFromDb.setParticipantsNumber(course.getParticipantsNumber());
                    }
                    if (course.getParticipantsLimit() != null) {
                        courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
                    }
                    if (course.getLessonsNumber() != null) {
                        courseFromDb.setLessonsNumber(course.getLessonsNumber());
                    }
                    if (course.getStartDate() != null) {
                        courseFromDb.setStartDate(course.getStartDate());
                    }
                    if (course.getEndDate() != null) {
                        courseFromDb.setEndDate(course.getEndDate());
                    }
                    return courseRepository.save(courseFromDb);
                }).orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    @Override
    public void deleteCourse(String id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
        courseRepository.deleteById(id);
    }

    @Override
    public void studentCourseEnrollment(String courseId, Long studentId) {
        Course course = getCourseById(courseId);
        validateCourseStatus(course);
        StudentDto studentDto = studentServiceClient.getStudentById(studentId);
        validateStudentBeforeCourseEnrollment(course, studentDto);
        course.incrementParticipantsNumber();
        CourseStudents courseStudents = new CourseStudents(studentDto.getId());
        studentServiceClient.courseEnrollment(studentId, course.getName());
        course.getCourseStudents().add(courseStudents);
        courseRepository.save(course);
    }

    private void validateStudentBeforeCourseEnrollment(Course course, StudentDto studentDto) {
        if (!Status.ACTIVE.equals(studentDto.getStatus())) {
            throw new CourseException(CourseError.STUDENT_IS_NOT_ACTIVE);
        }
        if (course.getCourseStudents()
                .stream()
                .anyMatch((member -> studentDto.getId().equals(member.getStudentId())))) {
            throw new CourseException(CourseError.STUDENT_ALREADY_ENROLLED);
        }
    }


    private void validateCourseStatus(Course course) {
        if (!Status.ACTIVE.equals(course.getStatus())) {
            throw new CourseException(CourseError.COURSE_IS_NOT_ACTIVE);
        }
    }

    @Override
    public void studentRemoveFromCourse(String courseId, Long studentId) {
        Course courseFromDb = getCourseById(courseId);
        List<CourseStudents> courseStudentsList = courseFromDb.getCourseStudents();
        boolean removed = courseStudentsList.removeIf(student -> studentId.equals(student.getStudentId()));

        if (!removed) {
            throw new CourseException(CourseError.STUDENT_IS_NOT_FOUND);
        }
        courseFromDb.setCourseStudents(courseStudentsList);
        courseFromDb.decrementParticipantsNumber();
        courseRepository.save(courseFromDb);
        studentServiceClient.courseUnEnrollment(studentId, courseFromDb.getName());
    }

    @Override
    public void teacherCourseEnrollment(String courseId, String teacherId) {

    }

    @Override
    public void teacherRemoveFromCourse(String courseId, String teacherId) {

    }
}
