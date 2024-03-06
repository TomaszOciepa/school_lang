package com.tom.calendarservice.service;

import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.repo.CalendarRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


class CalendarServiceImplTest {

    @Mock
    private CalendarRepository calendarRepository;
    @InjectMocks
    private CalendarServiceImpl calendarServiceImpl;

    List<Calendar> prepareCalendarData(){
        return Arrays.asList(
                new Calendar("1111D", "Lekcja Angielskiego",
                        LocalDateTime.of(2023, 12, 5, 16, 00),
                        LocalDateTime.of(2023, 12, 5, 17, 00),
                        1L, "212", Status.ACTIVE,"opis lekcji", new ArrayList<>()),
                new Calendar("1111D", "Lekcja Niemieckiego",
                        LocalDateTime.of(2023, 12, 15, 16, 00),
                        LocalDateTime.of(2023, 12, 15, 17, 00),
                        1L, "212", Status.ACTIVE,"opis lekcji", new ArrayList<>())
        );
    }

    Calendar prepareCalendar(){
        return new Calendar("1111D", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 5, 16, 00),
                LocalDateTime.of(2023, 12, 5, 17, 00),
                1L, "212", Status.ACTIVE,"opis lekcji", new ArrayList<>());
    }

    @Test
    void getAllLessons() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Calendar> mockCalendarList = prepareCalendarData();
        given(calendarRepository.findAll()).willReturn(mockCalendarList);
        //when
        List<Calendar> result = calendarServiceImpl.getAllLessons();
        //then
        assertEquals(mockCalendarList, result);
    }

    @Test
    void getLessonById() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111D";
        Calendar mockCalendar = prepareCalendar();
        given(calendarRepository.findById(mockId)).willReturn(Optional.ofNullable(mockCalendar));
        //when
        Calendar result = calendarServiceImpl.getLessonById(mockId);
        //then
        assertEquals(mockCalendar, result);
    }

    @Test
    void addLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockCalendar = prepareCalendar();
        given(calendarRepository.save(mockCalendar)).willReturn(mockCalendar);
        //when
        Calendar result = calendarServiceImpl.addLesson(mockCalendar);
        //then
        assertEquals(mockCalendar, result);
    }

    @Test
    void putLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockCalendar = prepareCalendar();
        String mockId = "1111D";
        given(calendarRepository.findById(mockId)).willReturn(Optional.ofNullable(mockCalendar));
        given(calendarRepository.save(mockCalendar)).willReturn(mockCalendar);
        //when
        Calendar result = calendarServiceImpl.putLesson(mockId, mockCalendar);
        //then
        assertEquals(mockCalendar, result);
    }

    @Test
    void putLessonShouldBeReturnExceptionCalendarNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111D";
        Calendar mockCalendar = prepareCalendar();
        CalendarException mockException = new CalendarException(CalendarError.CALENDAR_NOT_FOUND);
        given(calendarRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CalendarException.class, ()-> calendarServiceImpl.putLesson(mockId, mockCalendar));
    }

    @Test
    void patchLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockCalendar = prepareCalendar();
        String mockId = "1111D";
        given(calendarRepository.findById(mockId)).willReturn(Optional.ofNullable(mockCalendar));
        given(calendarRepository.save(mockCalendar)).willReturn(mockCalendar);
        //when
        Calendar result = calendarServiceImpl.patchLesson(mockId, mockCalendar);
        //then
        assertEquals(mockCalendar, result);
    }

    @Test
    void patchLessonShouldBeReturnCalendarNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111D";
        Calendar mockCalendar = prepareCalendar();
        CalendarException mockException = new CalendarException(CalendarError.CALENDAR_NOT_FOUND);
        given(calendarRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CalendarException.class, ()-> calendarServiceImpl.patchLesson(mockId, mockCalendar));
    }

    @Test
    void deleteLessonVerifyMethod() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111D";
        Calendar mockCalendar = prepareCalendar();
        given(calendarRepository.findById(mockId)).willReturn(Optional.ofNullable(mockCalendar));
        //when
        calendarServiceImpl.deleteLessonsById(mockId);
        //then
        verify(calendarRepository, times(1)).findById(mockId);
        verify(calendarRepository, times(1)).deleteById(mockId);
    }

    @Test
    void deleteLessonShouldBeReturnCalenderNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111D";
        Calendar mockCalendar = prepareCalendar();
        CalendarException mockException = new CalendarException(CalendarError.CALENDAR_NOT_FOUND);
        given(calendarRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CalendarException.class, ()-> calendarServiceImpl.deleteLessonsById(mockId));
    }
}