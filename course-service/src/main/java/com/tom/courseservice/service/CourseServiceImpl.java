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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentServiceClient studentServiceClient;
    private final TeacherServiceClient teacherServiceClient;

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
            throw new CourseException(CourseError.STUDENT_NO_ON_THE_LIST_OF_ENROLL);
        }
        courseFromDb.setCourseStudents(courseStudentsList);
        courseFromDb.decrementParticipantsNumber();
        courseRepository.save(courseFromDb);
    }

    @Override
    public void teacherCourseEnrollment(String courseId, Long teacherId) {
        Course course = getCourseById(courseId);
        validateCourseStatus(course);
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
        Course courseFromDb = getCourseById(courseId);
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
        Course course = getCourseById(courseId);
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
        Course course = getCourseById(courseId);
        List<CourseTeachers> courseTeachers = course.getCourseTeachers();
        if (courseTeachers.isEmpty()) {
            throw new CourseException(CourseError.COURSE_TEACHER_LIST_IS_EMPTY);
        }
        List<Long> idNumbers = courseTeachers.stream()
                .map(CourseTeachers::getTeacherId)
                .collect(Collectors.toList());

        return teacherServiceClient.getTeachersByIdNumber(idNumbers);
    }
}
