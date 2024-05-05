package com.tom.calendarservice.service;

import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Dto.CourseDto;
import com.tom.calendarservice.model.Dto.CourseTeachersDto;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.repo.CalendarRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


class CalendarServiceImplTest {
    private static Logger logger = LoggerFactory.getLogger(CalendarServiceImplTest.class);
    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private CourseServiceClient courseServiceClient;
    @Mock
    private TeacherServiceClient teacherServiceClient;
    @InjectMocks
    private CalendarServiceImpl calendarServiceImpl;

    List<Calendar> prepareCalendarData() {
        return Arrays.asList(
                new Calendar("1111D", "Lekcja Angielskiego",
                        LocalDateTime.of(2023, 12, 5, 16, 00),
                        LocalDateTime.of(2023, 12, 5, 17, 00),
                        1L, "212", Status.ACTIVE, "opis lekcji", new ArrayList<>()),
                new Calendar("1111D", "Lekcja Niemieckiego",
                        LocalDateTime.of(2023, 12, 15, 16, 00),
                        LocalDateTime.of(2023, 12, 15, 17, 00),
                        1L, "212", Status.ACTIVE, "opis lekcji", new ArrayList<>())
        );
    }

    Calendar prepareCalendar() {
        return new Calendar("65e7277467307d687b635b3c", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 5, 16, 00),
                LocalDateTime.of(2023, 12, 5, 17, 00),
                1L, "65db2e090f93ba7046abef53", Status.ACTIVE, "opis lekcji", new ArrayList<>());
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
        assertEquals(mockCalendarList.size(), result.size());
        verify(calendarRepository).findAll();
    }

    @Test
    void getLessonById_shouldReturnLesson_whenExists() {
        MockitoAnnotations.openMocks(this);
        //given
        String lessonId = "65e7277467307d687b635b3c";
        Calendar mockCalendar = prepareCalendar();
        given(calendarRepository.findById(lessonId)).willReturn(Optional.of(mockCalendar));
        //when
        Calendar result = calendarServiceImpl.getLessonById(lessonId);
        //then
        assertNotNull(result);
        assertEquals(mockCalendar, result);
        verify(calendarRepository).findById(lessonId);
    }

    @Test
    void getLessonById_shouldThrowException_whenNotExists() {
        MockitoAnnotations.openMocks(this);
        // given
        String lessonId = "nonExistingId";
        when(calendarRepository.findById(lessonId)).thenThrow(CalendarException.class);

        // then
        assertThrows(CalendarException.class, () -> calendarServiceImpl.getLessonById(lessonId));
        verify(calendarRepository).findById(lessonId);
    }

    @Test
    void addLessonWhenCourseIdExists_shouldBeReturn_newLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar newLesson = prepareCalendar();
        String courseId = "65db2e090f93ba7046abef53";
        CourseDto courseMock = new CourseDto("65db2e090f93ba7046abef53", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                LocalDateTime.of(2023, 12, 5, 00, 00),
                LocalDateTime.of(2024, 03, 6, 00, 00),
                Arrays.asList(), Arrays.asList(new CourseTeachersDto(1L,  LocalDateTime.of(2023, 12, 5, 16, 00), Status.ACTIVE)));

        given(calendarRepository.save(newLesson)).willReturn(newLesson);
        given(courseServiceClient.getCourseById(courseId, null)).willReturn(courseMock);

        //when
        Calendar result = calendarServiceImpl.addLesson(newLesson);
        //then
        assertEquals(newLesson, result);
        verify(courseServiceClient).getCourseById(courseId, null);
        verify(teacherServiceClient).teacherIsActive(1L);
    }

    @Test
    void addLessonWhenCourseIdNotExists_shouldBeReturn_newLesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar newLesson = new Calendar("65e7277467307d687b635b3c", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 5, 16, 00),
                LocalDateTime.of(2023, 12, 5, 17, 00),
                1L, "", Status.ACTIVE, "opis lekcji", new ArrayList<>());


        given(calendarRepository.save(newLesson)).willReturn(newLesson);
        //when
        Calendar result = calendarServiceImpl.addLesson(newLesson);
        //then
        assertEquals(newLesson, result);;
        verify(teacherServiceClient).teacherIsActive(1L);
    }

    @Test
    void addLessonWhenStartDateIsAfterEndDate_shouldBeReturn_Exception() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar newLesson = new Calendar("65e7277467307d687b635b3c", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 5, 18, 00),
                LocalDateTime.of(2023, 12, 5, 17, 00),
                1L, "", Status.ACTIVE, "opis lekcji", new ArrayList<>());


        given(calendarRepository.save(newLesson)).willThrow(CalendarException.class);

        //when & then
        assertThrows(CalendarException.class, ()->calendarServiceImpl.addLesson(newLesson));
    }
    @Test
    void patchLesson_shouldBeReturn_lesson() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockCalendar = prepareCalendar();
        String lessonId = "1111D";
        String courseId = "65db2e090f93ba7046abef53";
        CourseDto courseMock = new CourseDto("65db2e090f93ba7046abef53", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                LocalDateTime.of(2023, 12, 5, 00, 00),
                LocalDateTime.of(2024, 03, 6, 00, 00),
                Arrays.asList(), Arrays.asList(new CourseTeachersDto(1L,  LocalDateTime.of(2023, 12, 5, 16, 00), Status.ACTIVE)));

        given(calendarRepository.findById(lessonId)).willReturn(Optional.ofNullable(mockCalendar));
        given(courseServiceClient.getCourseById(courseId, null)).willReturn(courseMock);
        given(calendarRepository.save(mockCalendar)).willReturn(mockCalendar);
        //when
        Calendar result = calendarServiceImpl.patchLesson(lessonId, mockCalendar);
        //then
        assertEquals(mockCalendar, result);
        verify(courseServiceClient, times(3)).getCourseById(courseId, null);
    }

    @Test
    void patchLesson_WhenLessonStartIsBeforeCourseStartDate_shouldBeReturn_Exception() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockCalendar = new Calendar("65e7277467307d687b635b3c", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 4, 16, 00),
                LocalDateTime.of(2023, 12, 5, 17, 00),
                1L, "65db2e090f93ba7046abef53", Status.ACTIVE, "opis lekcji", new ArrayList<>());
        String lessonId = "65e7277467307d687b635b3c";
        String courseId = "65db2e090f93ba7046abef53";
        CourseDto courseMock = new CourseDto("65db2e090f93ba7046abef53", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                LocalDateTime.of(2023, 12, 5, 00, 00),
                LocalDateTime.of(2024, 03, 6, 00, 00),
                Arrays.asList(), Arrays.asList(new CourseTeachersDto(1L,  LocalDateTime.of(2023, 12, 5, 16, 00), Status.ACTIVE)));

        given(calendarRepository.findById(lessonId)).willReturn(Optional.ofNullable(mockCalendar));
        given(courseServiceClient.getCourseById(courseId, null)).willReturn(courseMock);
        given(calendarRepository.save(mockCalendar)).willReturn(mockCalendar).willThrow(CalendarException.class);


        //then
        assertThrows(CalendarException.class, ()->calendarServiceImpl.patchLesson(lessonId, mockCalendar));
    }

    @Test
    void patchLesson_WhenLessonStartIsAfterCourseEndDate_shouldBeReturn_Exception() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockCalendar = new Calendar("65e7277467307d687b635b3c", "Lekcja Angielskiego",
                LocalDateTime.of(2024, 03, 7, 16, 00),
                LocalDateTime.of(2024, 03, 7, 17, 00),
                1L, "65db2e090f93ba7046abef53", Status.ACTIVE, "opis lekcji", new ArrayList<>());
        String lessonId = "65e7277467307d687b635b3c";
        String courseId = "65db2e090f93ba7046abef53";
        CourseDto courseMock = new CourseDto("65db2e090f93ba7046abef53", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                LocalDateTime.of(2023, 12, 5, 00, 00),
                LocalDateTime.of(2024, 03, 6, 23, 59),
                Arrays.asList(), Arrays.asList(new CourseTeachersDto(1L,  LocalDateTime.of(2023, 12, 5, 16, 00), Status.ACTIVE)));

        given(calendarRepository.findById(lessonId)).willReturn(Optional.ofNullable(mockCalendar));
        given(courseServiceClient.getCourseById(courseId, null)).willReturn(courseMock);
        given(calendarRepository.save(mockCalendar)).willReturn(mockCalendar).willThrow(CalendarException.class);

        //then
        assertThrows(CalendarException.class, ()->calendarServiceImpl.patchLesson(lessonId, mockCalendar));
    }

    @Test
    void patchLesson_WhenLessonStartDateIsAfterLessonEndDate_shouldBeReturn_Exception() {
        MockitoAnnotations.openMocks(this);
        //given
        Calendar mockCalendar = new Calendar("65e7277467307d687b635b3c", "Lekcja Angielskiego",
                LocalDateTime.of(2023, 12, 6, 16, 00),
                LocalDateTime.of(2023, 12, 4, 17, 00),
                1L, "65db2e090f93ba7046abef53", Status.ACTIVE, "opis lekcji", new ArrayList<>());
        String lessonId = "65e7277467307d687b635b3c";
        String courseId = "65db2e090f93ba7046abef53";
        CourseDto courseMock = new CourseDto("65db2e090f93ba7046abef53", "Kurs Angielski-B2", Status.ACTIVE, 20L, 0L, 10L,
                LocalDateTime.of(2023, 12, 5, 00, 00),
                LocalDateTime.of(2024, 03, 6, 23, 59),
                Arrays.asList(), Arrays.asList(new CourseTeachersDto(1L,  LocalDateTime.of(2023, 12, 5, 16, 00), Status.ACTIVE)));

        given(calendarRepository.findById(lessonId)).willReturn(Optional.ofNullable(mockCalendar));
        given(courseServiceClient.getCourseById(courseId, null)).willReturn(courseMock);
        given(calendarRepository.save(mockCalendar)).willReturn(mockCalendar).willThrow(CalendarException.class);

        //then
        assertThrows(CalendarException.class, ()->calendarServiceImpl.patchLesson(lessonId, mockCalendar));
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
        assertThrows(CalendarException.class, () -> calendarServiceImpl.patchLesson(mockId, mockCalendar));
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
        assertThrows(CalendarException.class, () -> calendarServiceImpl.deleteLessonsById(mockId));
    }
}