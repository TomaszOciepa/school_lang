package com.tom.calendarservice.controller;

import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.service.CalendarService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;
    @InjectMocks
    private CalendarController calendarController;

    List<Calendar> prepareCalendarData(){
        return Arrays.asList(
                new Calendar("1111D", "Lekcja Angielskiego",
                        LocalDateTime.of(2023, 12, 5, 16, 00),
                        LocalDateTime.of(2023, 12, 5, 17, 00),
                        1L, "212", "Opis lekcji", new ArrayList<>()),
                new Calendar("1111D", "Lekcja Niemieckiego",
                        LocalDateTime.of(2023, 12, 15, 16, 00),
                        LocalDateTime.of(2023, 12, 15, 17, 00),
                        1L, "212", "Opis lekcji", new ArrayList<>())
        );
    }

    Calendar prepareCalendar(){
        return new Calendar("1111D", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 5, 16, 00),
                LocalDateTime.of(2023, 12, 5, 17, 00),
                1L, "212", "Opis lekcji", new ArrayList<>());
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
    void putLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockLessons = prepareCalendar();
        String mockId = "1111D";
        given(calendarService.putLesson(mockId, mockLessons)).willReturn(mockLessons);
        //when
        Calendar result = calendarController.putLesson(mockId, mockLessons);
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
    void deleteLessonsByIdVerifyMethod() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111D";
        //when
        calendarController.deleteLessonsById(mockId);
        //then
        verify(calendarService, times(1)).deleteLesson(mockId);
    }
}