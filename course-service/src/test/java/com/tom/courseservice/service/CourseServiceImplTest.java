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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentServiceClient studentServiceClient;
    @Mock
    private TeacherServiceClient teacherServiceClient;
    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    List<Course> prepareCourseData() {
        return Arrays.asList(
                new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L, 0L,
                        LocalDate.of(2023, 12, 5),
                        LocalDate.of(2024, 03, 12),
                        Arrays.asList(), Arrays.asList()),
                new Course("1111", "Kurs Niemiecki", Status.ACTIVE, 20L, 0L, 12L, 0L,
                        LocalDate.of(2023, 12, 8),
                        LocalDate.of(2024, 02, 12),
                        Arrays.asList(), Arrays.asList())
        );
    }

    Course prepareCourse() {
        return new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L, 0L,
                LocalDate.of(2023, 12, 5),
                LocalDate.of(2024, 03, 12),
                new ArrayList<>(), new ArrayList<>());
    }

    StudentDto prepareStudent() {
        return new StudentDto(1L, "Tomek", "Kowalski", "kow@wp.pl", Status.ACTIVE);
    }

    List<StudentDto> prepareStudentList(){
        return Arrays.asList(
                new StudentDto(1L, "Tomek", "Kowalski", "kow@wp.pl", Status.ACTIVE)
        );
    }

    TeacherDto prepareTeacher() {
        return new TeacherDto(1L, "Klaudia", "Nowak", "kla@wp.pl", Status.ACTIVE);
    }

    @Test
    void findAllByWithStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Course> mockCourses = prepareCourseData();
        Status mockStatus = Status.ACTIVE;
        given(courseRepository.findAllByStatus(mockStatus)).willReturn(mockCourses);
        //when
        List<Course> result = courseServiceImpl.findAllByStatus(mockStatus);
        //then
        assertEquals(mockCourses, result);
    }

    @Test
    void findAllByWithoutStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Course> mockCourses = prepareCourseData();
        given(courseRepository.findAll()).willReturn(mockCourses);
        //when
        List<Course> result = courseServiceImpl.findAllByStatus(null);
        //then
        assertEquals(mockCourses, result);
    }

    @Test
    void getCourseByIdShouldBeReturnCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        String mockId = "1111";
        given(courseRepository.findById(mockId)).willReturn(Optional.ofNullable(mockCourse));
        //when
        Course result = courseServiceImpl.findByIdAndStatus(mockId, null);
        //then
        assertEquals(mockCourse, result);
    }

    @Test
    void getCourseByIdShouldBeReturnExceptionCourseNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111";
        CourseException mockException = new CourseException(CourseError.COURSE_NOT_FOUND);
        given(courseRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CourseException.class, () -> courseServiceImpl.findByIdAndStatus(mockId, null));
    }

    @Test
    void addCourseShouldBeReturnCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        given(courseRepository.existsByName(mockCourse.getName())).willReturn(false);
        given(courseRepository.save(mockCourse)).willReturn(mockCourse);
        //when
        Course result = courseServiceImpl.addCourse(mockCourse);
        //then
        assertEquals(mockCourse, result);
    }

    @Test
    void addCourseShouldBeReturnExceptionCourseNameAlreadyExists() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        CourseException mockException = new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
        given(courseRepository.existsByName(mockCourse.getName())).willThrow(mockException);
        //when
        //then
        assertThrows(CourseException.class, () -> courseServiceImpl.addCourse(mockCourse));
    }

    @Test
    void putCourseShouldBeReturnCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        String mockId = "1111";
        given(courseRepository.findById(mockId)).willReturn(Optional.ofNullable(mockCourse));
        given(courseRepository.save(mockCourse)).willReturn(mockCourse);
        //when
        Course result = courseServiceImpl.putCourse(mockId, mockCourse);
        //then
        assertEquals(mockCourse, result);
    }

    @Test
    void putCourseShouldBeReturnExceptionCourseNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        String mockId = "1111";
        CourseException mockException = new CourseException(CourseError.COURSE_NOT_FOUND);
        given(courseRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CourseException.class, () -> courseServiceImpl.putCourse(mockId, mockCourse));
    }

    @Test
    void putCourseShouldBeReturnExceptionCourseNameAlreadyExists() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        String mockId = "1111";
        CourseException mockException = new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
        given(courseRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CourseException.class, () -> courseServiceImpl.putCourse(mockId, mockCourse));
    }

    @Test
    void patchCourseShouldBeReturnCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mokCourse = prepareCourse();
        String mockId = "1111";
        given(courseRepository.findById(mockId)).willReturn(Optional.ofNullable(mokCourse));
        given(courseRepository.save(mokCourse)).willReturn(mokCourse);
        //when
        Course result = courseServiceImpl.patchCourse(mockId, mokCourse);
        //then
        assertEquals(mokCourse, result);
    }

    @Test
    void patchCourseShouldBeReturnExceptionCourseNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mokCourse = prepareCourse();
        String mockId = "1111";
        CourseException mockException = new CourseException(CourseError.COURSE_NOT_FOUND);
        given(courseRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CourseException.class, () -> courseServiceImpl.patchCourse(mockId, mokCourse));
    }

    @Test
    void deleteCourseVerifyMethod() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111";
        Course mockCourse = prepareCourse();
        mockCourse.setId(mockId);
        given(courseRepository.findById(mockId)).willReturn(Optional.of(mockCourse));
        //when
        courseServiceImpl.deleteCourse(mockId);
        //then
        verify(courseRepository, times(1)).findById(mockId);
        verify(courseRepository, times(1)).deleteById(mockId);
    }

    @Test
    void deleteCourseShouldBeReturnCourseNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111";
        Course mockCourse = prepareCourse();
        mockCourse.setId(mockId);
        CourseException mockException = new CourseException(CourseError.COURSE_NOT_FOUND);
        given(courseRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CourseException.class, () -> courseServiceImpl.deleteCourse(mockId));
    }

    @Test
    void studentCourseEnrollment() {
        MockitoAnnotations.openMocks(this);
        //given
        String courseIdMock = "1111";
        Course courseMock = prepareCourse();
        StudentDto studentMock = prepareStudent();
        Long studentIdMock = 1L;
        CourseStudents courseStudents = new CourseStudents(studentMock.getId(), Status.ACTIVE);
        given(courseRepository.findById(courseIdMock)).willReturn(Optional.ofNullable(courseMock));
        given(studentServiceClient.getStudentById(studentIdMock)).willReturn(studentMock);
        //when
        courseServiceImpl.studentCourseEnrollment(courseIdMock, studentIdMock);
        //then
        verify(courseRepository, times(1)).findById(courseIdMock);
        verify(studentServiceClient, times(1)).getStudentById(studentIdMock);
        verify(courseRepository, times(1)).save(courseMock);
    }

    @Test
    void studentRemoveFromCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        StudentDto studentMock = prepareStudent();
        Long studentIdMock = 1L;
        CourseStudents courseStudentsMock = new CourseStudents(studentMock.getId(), Status.ACTIVE);
        String courseIdMock = "1111";
        Course courseMock = prepareCourse();
        courseMock.getCourseStudents().add(courseStudentsMock);
        courseMock.incrementParticipantsNumber();
        given(courseRepository.findById(courseIdMock)).willReturn(Optional.ofNullable(courseMock));
        //when
        courseServiceImpl.studentRemoveFromCourse(courseIdMock, studentIdMock);
        //then
        verify(courseRepository, times(1)).findById(courseIdMock);
        verify(courseRepository, times(1)).save(courseMock);
        assertEquals(0, courseMock.getParticipantsNumber());
        assertTrue(courseMock.getCourseStudents().isEmpty());
    }

    @Test
    void teacherCourseEnrollment() {
        MockitoAnnotations.openMocks(this);
        //given
        String courseIdMock = "1111";
        Course courseMock = prepareCourse();
        TeacherDto teacherMock = prepareTeacher();
        Long teacherIdMock = 1L;
        given(courseRepository.findById(courseIdMock)).willReturn(Optional.ofNullable(courseMock));
        given(teacherServiceClient.getTeacherById(teacherIdMock)).willReturn(teacherMock);
        //when
        courseServiceImpl.teacherCourseEnrollment(courseIdMock, teacherIdMock);
        //then
        verify(courseRepository, times(1)).findById(courseIdMock);
        verify(teacherServiceClient, times(1)).getTeacherById(teacherIdMock);
        verify(courseRepository, times(1)).save(courseMock);
    }

    @Test
    void teacherRemoveFromCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        TeacherDto teacherMock = prepareTeacher();
        Long teacherIdMock = 1L;
        CourseTeachers courseTeachersMock = new CourseTeachers(teacherMock.getId());
        String courseIdMock = "1111";
        Course courseMock = prepareCourse();
        courseMock.getCourseTeachers().add(courseTeachersMock);
        given(courseRepository.findById(courseIdMock)).willReturn(Optional.ofNullable(courseMock));
        //when
        courseServiceImpl.teacherRemoveFromCourse(courseIdMock, teacherIdMock);
        //then
        verify(courseRepository, times(1)).findById(courseIdMock);
        verify(courseRepository, times(1)).save(courseMock);
        assertTrue(courseMock.getCourseTeachers().isEmpty());
    }
}