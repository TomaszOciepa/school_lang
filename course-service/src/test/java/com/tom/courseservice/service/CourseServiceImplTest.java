package com.tom.courseservice.service;

import com.tom.courseservice.exception.CourseError;
import com.tom.courseservice.exception.CourseException;
import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.repo.CourseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    List<Course> prepareCourseData() {
        return Arrays.asList(
                new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                        LocalDateTime.of(2023, 12, 5, 0, 0),
                        LocalDateTime.of(2024, 03, 12, 0, 0),
                        Arrays.asList(), Arrays.asList()),
                new Course("1111", "Kurs Niemiecki", Status.ACTIVE, 20L, 0L, 12L,
                        LocalDateTime.of(2023, 12, 8, 0, 0),
                        LocalDateTime.of(2024, 02, 12, 0, 0),
                        Arrays.asList(), Arrays.asList())
        );
    }

    Course prepareCourse() {
        return new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                LocalDateTime.of(2023, 12, 5, 0, 0),
                LocalDateTime.of(2024, 03, 12, 0, 0),
                Arrays.asList(), Arrays.asList());
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
        Course result = courseServiceImpl.getCourseById(mockId);
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
        assertThrows(CourseException.class, ()-> courseServiceImpl.getCourseById(mockId));
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
        assertThrows(CourseException.class, ()-> courseServiceImpl.addCourse(mockCourse));
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
        assertThrows(CourseException.class, ()-> courseServiceImpl.putCourse(mockId, mockCourse));
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
        assertThrows(CourseException.class, ()-> courseServiceImpl.putCourse(mockId, mockCourse));
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
        assertThrows(CourseException.class, ()-> courseServiceImpl.patchCourse(mockId, mokCourse));
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
        assertThrows(CourseException.class, ()-> courseServiceImpl.deleteCourse(mockId));
    }

    @Test
    void studentCourseEnrollment() {
    }

    @Test
    void studentRemoveFromCourse() {
    }

    @Test
    void teacherCourseEnrollment() {
    }

    @Test
    void teacherRemoveFromCourse() {
    }
}