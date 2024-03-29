package com.tom.courseservice.controller;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Status;
import com.tom.courseservice.service.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CourseControllerTest {

    @Mock
    private CourseService courseService;
    @InjectMocks
    private CourseController courseController;

    List<Course> prepareCourseData() {
        return Arrays.asList(
                new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                        LocalDateTime.of(2023, 12, 5, 00, 00),
                        LocalDateTime.of(2024, 03, 6, 00, 00),
                        Arrays.asList(), Arrays.asList()),
                new Course("1111", "Kurs Angielski-B1", Status.ACTIVE, 20L, 0L, 10L,
                        LocalDateTime.of(2023, 12, 5, 00, 00),
                        LocalDateTime.of(2024, 03, 6, 00, 00),
                        Arrays.asList(), Arrays.asList())

        );
    }

    Course prepareCourse() {
        return new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                LocalDateTime.of(2023, 12, 5, 00, 00),
                LocalDateTime.of(2024, 03, 6, 00, 00),
                Arrays.asList(), Arrays.asList());
    }

    @Test
    void findAllByWithStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Course> mockCourseList = prepareCourseData();
        Status mockStatus = Status.ACTIVE;
        given(courseService.getAllByStatus(mockStatus)).willReturn(mockCourseList);
        //when
        List<Course> result = courseController.getAllByStatus(mockStatus);
        //then
        assertEquals(mockCourseList, result);
    }

    @Test
    void findAllByWithoutStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Course> mockCourseList = prepareCourseData();
        Status mockStatus = null;
        given(courseService.getAllByStatus(mockStatus)).willReturn(mockCourseList);
        //when
        List<Course> result = courseController.getAllByStatus(mockStatus);
        //then
        assertEquals(mockCourseList, result);
    }

    @Test
    void getCourseById() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        String mockId = "1111";
        given(courseService.getCourseById(mockId, null)).willReturn(mockCourse);
        //when
        Course result = courseController.getCourseById(mockId, null);
        //then
        assertEquals(mockCourse, result);
    }

    @Test
    void addCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        given(courseService.addCourse(mockCourse)).willReturn(mockCourse);
        //when
        Course result = courseController.addCourse(mockCourse);
        //then
        assertEquals(mockCourse, result);
    }

    @Test
    void patchCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        Course mockCourse = prepareCourse();
        String mockId = "1111";
        given(courseService.patchCourse(mockId, mockCourse)).willReturn(mockCourse);
        //when
        Course result = courseController.patchCourse(mockId, mockCourse);
        //then
        assertEquals(mockCourse, result);
    }

    @Test
    void deleteCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111";
        willDoNothing().given(courseService).deleteCourseById(mockId);
        //when
        courseController.deleteCourseById(mockId);
        //then
        verify(courseService, times(1)).deleteCourseById(mockId);
    }
}