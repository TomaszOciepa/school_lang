package com.tom.calendarservice.service;

import com.tom.calendarservice.exception.CalendarError;
import com.tom.calendarservice.exception.CalendarException;
import com.tom.calendarservice.model.Calendar;
import com.tom.calendarservice.model.Language;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.repo.CalendarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CalendarServiceImplTest {

    @InjectMocks
    private CalendarServiceImpl calendarService;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private TeacherServiceClient teacherServiceClient;

    private Calendar lesson1;
    private Calendar lesson2;

    @BeforeEach
    void setUp() {
        lesson1 = createLesson("1", "Lekcja 1");
        lesson2 = createLesson("2", "Lekcja 2");
    }

    @Test
    void getAllLessons_ShouldReturnSortedAndUpdatedLessons() {
        // GIVEN
        List<Calendar> mockLessons = Arrays.asList(lesson1, lesson2);

        when(calendarRepository.findAllByOrderByStartDateAsc()).thenReturn(mockLessons);

        // WHEN
        List<Calendar> result = calendarService.getAllLessons();

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("1", result.get(0).getId());
        Assertions.assertEquals("2", result.get(1).getId());

        verify(calendarRepository, times(1)).findAllByOrderByStartDateAsc();
    }

    @Test
    void getLessonById_ShouldReturnLesson_WhenLessonExists() {
        // GIVEN
        String lessonId = "1";

        when(calendarRepository.findById(lessonId)).thenReturn(Optional.of(lesson1));

        // WHEN
        Calendar result = calendarService.getLessonById(lessonId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(lesson1.getId(), result.getId());
        Assertions.assertEquals(lesson1.getEventName(), result.getEventName());

        verify(calendarRepository, times(1)).findById(lessonId);
    }

    @Test
    void getLessonById_ShouldThrowCalendarException_WhenLessonDoesNotExist() {
        // GIVEN
        String lessonId = "999";

        // WHEN
        when(calendarRepository.findById(lessonId)).thenReturn(Optional.empty());

        // THEN
        Assertions.assertThrows(CalendarException.class, () -> {
            calendarService.getLessonById(lessonId);
        });
    }

    @Test
    void getLessonsByCourseId_ShouldReturnLessons_WhenLessonsExist() {
        // GIVEN
        String courseId = "1233";

        when(calendarRepository.findByCourseIdOrderByStartDateAsc(courseId)).thenReturn(Arrays.asList(lesson1, lesson2));

        // WHEN
        List<Calendar> result = calendarService.getLessonsByCourseId(courseId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(lesson1.getId(), result.get(0).getId());
        Assertions.assertEquals(lesson2.getId(), result.get(1).getId());

        verify(calendarRepository, times(1)).findByCourseIdOrderByStartDateAsc(courseId);
    }

    @Test
    void getLessonsByCourseId_ShouldReturnEmptyList_WhenNoLessonsExist() {
        // GIVEN
        String courseId = "9999";

        when(calendarRepository.findByCourseIdOrderByStartDateAsc(courseId)).thenReturn(Collections.emptyList());

        // WHEN
        List<Calendar> result = calendarService.getLessonsByCourseId(courseId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty(), "The list should be empty when no lessons exist.");

        verify(calendarRepository, times(1)).findByCourseIdOrderByStartDateAsc(courseId);
    }

    @Test
    void getLessonsNumberByCourseId_ShouldReturnNumberOfLessons_WhenLessonsExist() {
        // GIVEN
        String courseId = "1233";

        when(calendarRepository.findByCourseIdOrderByStartDateAsc(courseId)).thenReturn(Arrays.asList(lesson1, lesson2));

        // WHEN
        int result = calendarService.getLessonsNumberByCourseId(courseId);

        // THEN
        Assertions.assertEquals(2, result);

        verify(calendarRepository, times(1)).findByCourseIdOrderByStartDateAsc(courseId);
    }

    @Test
    void getLessonsNumberByCourseId_ShouldReturnZero_WhenNoLessonsExist() {
        // GIVEN
        String courseId = "9999";

        when(calendarRepository.findByCourseIdOrderByStartDateAsc(courseId)).thenReturn(Collections.emptyList());

        // WHEN
        int result = calendarService.getLessonsNumberByCourseId(courseId);

        // THEN
        Assertions.assertEquals(0, result);

        verify(calendarRepository, times(1)).findByCourseIdOrderByStartDateAsc(courseId);
    }

    @Test
    void getLessonsByTeacherId_ShouldReturnLessons_WhenLessonsExist() {
        // GIVEN
        Long teacherId = 1L;

        List<Calendar> mockLessons = Arrays.asList(lesson1, lesson2);

        when(calendarRepository.findByTeacherIdOrderByStartDateAsc(teacherId)).thenReturn(mockLessons);

        // WHEN
        List<Calendar> result = calendarService.getLessonsByTeacherId(teacherId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(lesson1.getId(), result.get(0).getId());
        Assertions.assertEquals(lesson2.getId(), result.get(1).getId());

        verify(calendarRepository, times(1)).findByTeacherIdOrderByStartDateAsc(teacherId);
    }

    @Test
    void getLessonsByTeacherId_ShouldReturnEmptyList_WhenNoLessonsFound() {
        // GIVEN
        Long teacherId = 1L;
        List<Calendar> emptyLessons = Collections.emptyList();

        when(calendarRepository.findByTeacherIdOrderByStartDateAsc(teacherId)).thenReturn(emptyLessons);

        // WHEN
        List<Calendar> result = calendarService.getLessonsByTeacherId(teacherId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        verify(calendarRepository, times(1)).findByTeacherIdOrderByStartDateAsc(teacherId);
    }

    @Test
    void getLessonsByStudentId_ShouldReturnLessons_WhenLessonsExist() {
        // GIVEN
        Long studentId = 1L;
        List<Calendar> mockLessons = Arrays.asList(lesson1, lesson2);

        when(calendarRepository.findByAttendanceListStudentIdOrderByStartDateAsc(studentId)).thenReturn(mockLessons);

        // WHEN
        List<Calendar> result = calendarService.getLessonsByStudentId(studentId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(lesson1.getId(), result.get(0).getId());
        Assertions.assertEquals(lesson2.getId(), result.get(1).getId());

        verify(calendarRepository, times(1)).findByAttendanceListStudentIdOrderByStartDateAsc(studentId);
    }

    @Test
    void getLessonsByStudentId_ShouldThrowException_WhenNoLessonsFound() {
        // GIVEN
        Long studentId = 1L;

        when(calendarRepository.findByAttendanceListStudentIdOrderByStartDateAsc(studentId)).thenReturn(Collections.emptyList());

        // WHEN & THEN
        CalendarException thrownException = Assertions.assertThrows(CalendarException.class, () -> {
            calendarService.getLessonsByStudentId(studentId);
        });

        Assertions.assertEquals(CalendarError.CALENDAR_LESSONS_NOT_FOUND, thrownException.getCalendarError());

        verify(calendarRepository, times(1)).findByAttendanceListStudentIdOrderByStartDateAsc(studentId);
    }

    @Test
    void isTeacherAssignedToLessonInCourse_ShouldReturnTrue_WhenTeacherIsAssignedToLesson() {
        // GIVEN
        String courseId = "course123";
        Long teacherId = 1L;

        when(calendarRepository.findByCourseIdOrderByStartDateAsc(courseId)).thenReturn(Arrays.asList(lesson1, lesson2));

        // WHEN
        boolean result = calendarService.isTeacherAssignedToLessonInCourse(courseId, teacherId);

        // THEN
        Assertions.assertTrue(result);
    }

    @Test
    void isTeacherAssignedToLessonInCourse_ShouldReturnFalse_WhenTeacherIsNotAssignedToAnyLesson() {
        // GIVEN
        String courseId = "course123";
        Long teacherId = 2L;

        when(calendarRepository.findByCourseIdOrderByStartDateAsc(courseId)).thenReturn(Arrays.asList(lesson1, lesson2));

        // WHEN
        boolean result = calendarService.isTeacherAssignedToLessonInCourse(courseId, teacherId);

        // THEN

        Assertions.assertFalse(result);
    }

    @Test
    void isTeacherAssignedToLessonInCourse_ShouldReturnFalse_WhenNoLessonsExistForCourse() {
        // GIVEN
        String courseId = "course123";
        Long teacherId = 1L;

        when(calendarRepository.findByCourseIdOrderByStartDateAsc(courseId)).thenReturn(Collections.emptyList());

        // WHEN
        boolean result = calendarService.isTeacherAssignedToLessonInCourse(courseId, teacherId);

        // THEN
        Assertions.assertFalse(result);
    }


    private Calendar createLesson(String id, String eventName) {
        Calendar lesson = new Calendar();
        lesson.setId(id);
        lesson.setEventName(eventName);
        lesson.setStartDate(LocalDateTime.now());
        lesson.setEndDate(LocalDateTime.now().plusHours(1));
        lesson.setTeacherId(1L);
        lesson.setCourseId("1233");
        lesson.setStatus(Status.ACTIVE);
        lesson.setDescription("some description");
        lesson.setLanguage(Language.POLISH);
        lesson.setPrice(120L);
        return lesson;
    }
}