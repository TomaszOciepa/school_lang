package com.tom.calendarservice.controller;

import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.service.CalendarService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;
    @InjectMocks
    private CalendarController calendarController;

    List<Calendar> prepareCalendarData() {
        return Arrays.asList(
                new Calendar("1111D", "Lekcja Angielskiego",
                        LocalDateTime.of(2023, 12, 5, 16, 00),
                        LocalDateTime.of(2023, 12, 5, 17, 00),
                        1L, "212g54", Status.ACTIVE, "Opis lekcji", new ArrayList<>()),
                new Calendar("1111D", "Lekcja Niemieckiego",
                        LocalDateTime.of(2023, 12, 15, 16, 00),
                        LocalDateTime.of(2023, 12, 15, 17, 00),
                        1L, "212g54", Status.ACTIVE, "Opis lekcji", new ArrayList<>())
        );
    }

    Calendar prepareCalendar() {
        return new Calendar("1111D", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 5, 16, 00),
                LocalDateTime.of(2023, 12, 5, 17, 00),
                1L, "212", Status.ACTIVE, "Opis lekcji", new ArrayList<>());
    }

    @Test
    void getAllLessons() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Calendar> mockCalendarList = prepareCalendarData();
        given(calendarService.getAllLessons()).willReturn(mockCalendarList);
        //when
        List<Calendar> result = calendarController.getAllLessons();
        //then
        assertEquals(mockCalendarList, result);
    }

    @Test
    void getLessonById() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockLessons = prepareCalendar();
        String mockId = "1111D";
        given(calendarService.getLessonById(mockId)).willReturn(mockLessons);
        //when
        Calendar result = calendarController.getLessonById(mockId);
        //then
        assertEquals(mockLessons, result);
    }

    @Test
    void getLessonsByCourseId() {
        MockitoAnnotations.openMocks(this);
        //given
        String courseId = "212g54";
        List<Calendar> expectedLessons = prepareCalendarData();
        given(calendarService.getLessonsByCourseId(courseId)).willReturn(expectedLessons);

        //when
        List<Calendar> result = calendarController.getLessonsByCourseId(courseId);

        //then
        assertEquals(expectedLessons, result);
        verify(calendarService).getLessonsByCourseId(courseId);
    }

    @Test
    void getLessonsNumberByCourseId() {
        MockitoAnnotations.openMocks(this);
        //given
        int expectedLessonsNumber = 2;
        String courseId = "212g54";
        given(calendarService.getLessonsNumberByCourseId(courseId)).willReturn(expectedLessonsNumber);

        //when
        int result = calendarController.getLessonsNumberByCourseId(courseId);

        //then
        assertEquals(expectedLessonsNumber, result);
        verify(calendarService).getLessonsNumberByCourseId(courseId);
    }

    @Test
    void addLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockLessons = prepareCalendar();
        given(calendarService.addLesson(mockLessons)).willReturn(mockLessons);
        //when
        Calendar result = calendarController.addLesson(mockLessons);
        //then
        assertEquals(mockLessons, result);
    }

    @Test
    void patchLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockLessons = prepareCalendar();
        String mockId = "1111D";
        given(calendarService.patchLesson(mockId, mockLessons)).willReturn(mockLessons);
        //when
        Calendar result = calendarController.patchLesson(mockId, mockLessons);
        //then
        assertEquals(mockLessons, result);
    }

    @Test
    void deleteLessonsById() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111D";
        //when
        calendarController.deleteLessonsById(mockId);
        //then
        verify(calendarService, times(1)).deleteLessonsById(mockId);
    }

    @Test
    void deleteCourseLessons() {
        MockitoAnnotations.openMocks(this);
        //given
        String courseId = "212dd";

        //when
        calendarController.deleteCourseLessons(courseId);

        //then
        verify(calendarService, times(1)).deleteCourseLessons(courseId);
    }

    @Test
    void isTeacherAssignedToLessonInCourse() {
        MockitoAnnotations.openMocks(this);
        //given
        String courseId = "212dd";
        Long teacherId = 12L;
        boolean expectedResult = true;
        given(calendarService.isTeacherAssignedToLessonInCourse(courseId, teacherId)).willReturn(expectedResult);

        //when
        boolean result = calendarController.isTeacherAssignedToLessonInCourse(courseId, teacherId);

        //then
        assertEquals(expectedResult, result);
        verify(calendarService).isTeacherAssignedToLessonInCourse(courseId, teacherId);
    }

    @Test
    void enrollStudent() {
        MockitoAnnotations.openMocks(this);
        //given
        String courseId = "212dd";
        Long studentId = 12L;
        willDoNothing().given(calendarService).enrollStudent(courseId, studentId);

        //when
        ResponseEntity<?> responseEntity = calendarController.enrollStudent(courseId, studentId);

        //then
        verify(calendarService).enrollStudent(courseId, studentId);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void unEnrollStudent() {
        MockitoAnnotations.openMocks(this);
        //given
        String courseId = "212dd";
        Long teacherId = 12L;
        boolean expectedResult = true;
        given(calendarService.unEnrollStudent(courseId, teacherId)).willReturn(expectedResult);

        //when
        boolean result = calendarController.unEnrollStudent(courseId, teacherId);

        //then
        assertEquals(expectedResult, result);
        verify(calendarService).unEnrollStudent(courseId, teacherId);
    }

    @Test
    void enrollStudentLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        String lessonId = "212dd";
        Long studentId = 12L;
        willDoNothing().given(calendarService).enrollStudentLesson(lessonId, studentId);

        //when
        ResponseEntity<?> responseEntity = calendarController.enrollStudentLesson(lessonId, studentId);

        //then
        verify(calendarService).enrollStudentLesson(lessonId, studentId);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void unEnrollStudentLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        String lessonId = "212dd";
        Long studentId = 12L;
        willDoNothing().given(calendarService).unEnrollStudentLesson(lessonId, studentId);

        //when
        ResponseEntity<?> responseEntity = calendarController.unEnrollStudentLesson(lessonId, studentId);

        //then
        verify(calendarService).unEnrollStudentLesson(lessonId, studentId);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    void getLessonByStudentId() {
        MockitoAnnotations.openMocks(this);
        //given
        Long studentId = 12L;
        List<Calendar> expectedLessons = prepareCalendarData();
        given(calendarService.getLessonsByStudentId(studentId)).willReturn(expectedLessons);

        //when
        List<Calendar> result = calendarController.getLessonsByStudentId(studentId);

        //then
        assertEquals(expectedLessons, result);
        verify(calendarService).getLessonsByStudentId(studentId);
    }

    @Test
    void getLessonByTeacherId() {
        MockitoAnnotations.openMocks(this);
        //given
        Long teacherId = 1L;
        List<Calendar> expectedLessons = prepareCalendarData();
        given(calendarService.getLessonsByTeacherId(teacherId)).willReturn(expectedLessons);

        //when
        List<Calendar> result = calendarController.getLessonByTeacherId(teacherId);

        //then
        assertEquals(expectedLessons, result);
        verify(calendarService).getLessonsByTeacherId(teacherId);
    }
}