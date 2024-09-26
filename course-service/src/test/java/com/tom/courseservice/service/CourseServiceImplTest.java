package com.tom.courseservice.service;

import com.tom.courseservice.exception.CourseError;
import com.tom.courseservice.exception.CourseException;
import com.tom.courseservice.model.*;
import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.model.dto.TeacherDto;
import com.tom.courseservice.repo.CourseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentServiceClient studentServiceClient;
    @Mock
    private TeacherServiceClient teacherServiceClient;
    @Mock
    private CalendarServiceClient calendarServiceClient;
    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

//    List<Course> prepareCourseData() {
//        return Arrays.asList(
//                new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, Language.ENGLISH, 20L, 0L, 10L,
//                        LocalDateTime.of(2023, 12, 5, 17, 0),
//                        LocalDateTime.of(2024, 03, 12, 16,0),
//                        Arrays.asList(), Arrays.asList()),
//                new Course("1111", "Kurs Niemiecki", Status.ACTIVE, Language.GERMAN, 20L, 0L, 12L,
//                        LocalDateTime.of(2023, 12, 8, 8,0),
//                        LocalDateTime.of(2024, 02, 12, 20,0),
//                        Arrays.asList(), Arrays.asList())
//        );
//    }

//    Course prepareCourse() {
//        return new Course("1111", "Kurs Angielski-B2", Status.ACTIVE, Language.ENGLISH, 20L, 0L, 10L,
//                LocalDateTime.of(2023, 12, 5, 8,0),
//                LocalDateTime.of(2024, 03, 12, 20,0),
//                new ArrayList<>(), new ArrayList<>());
//    }

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
    void getCourseByIdShouldBeReturnExceptionCourseNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockId = "1111";
        CourseException mockException = new CourseException(CourseError.COURSE_NOT_FOUND);
        given(courseRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(CourseException.class, () -> courseServiceImpl.getCourseById(mockId, null));
    }

//    @Test
//    void addCourseShouldBeReturnCourse() {
//        MockitoAnnotations.openMocks(this);
//        //given
//        Course mockCourse = prepareCourse();
//        given(courseRepository.existsByName(mockCourse.getName())).willReturn(false);
//        given(courseRepository.save(mockCourse)).willReturn(mockCourse);
//        //when
//        Course result = courseServiceImpl.addCourse(mockCourse);
//        //then
//        assertEquals(mockCourse, result);
//    }

//    @Test
//    void addCourseShouldBeReturnExceptionCourseNameAlreadyExists() {
//        MockitoAnnotations.openMocks(this);
//        //given
//        Course mockCourse = prepareCourse();
//        CourseException mockException = new CourseException(CourseError.COURSE_NAME_ALREADY_EXISTS);
//        given(courseRepository.existsByName(mockCourse.getName())).willThrow(mockException);
//        //when
//        //then
//        assertThrows(CourseException.class, () -> courseServiceImpl.addCourse(mockCourse));
//    }

//    @Test
//    void patchCourseShouldBeReturnCourse() {
//        MockitoAnnotations.openMocks(this);
//        //given
//        Course mokCourse = prepareCourse();
//        String mockId = "1111";
//        given(courseRepository.findById(mockId)).willReturn(Optional.ofNullable(mokCourse));
//        given(courseRepository.save(mokCourse)).willReturn(mokCourse);
//        //when
//        Course result = courseServiceImpl.patchCourse(mockId, mokCourse);
//        //then
//        assertEquals(mokCourse, result);
//    }

//    @Test
//    void patchCourseShouldBeReturnExceptionCourseNotFound() {
//        MockitoAnnotations.openMocks(this);
//        //given
//        Course mokCourse = prepareCourse();
//        String mockId = "1111";
//        CourseException mockException = new CourseException(CourseError.COURSE_NOT_FOUND);
//        given(courseRepository.findById(mockId)).willThrow(mockException);
//        //when
//        //then
//        assertThrows(CourseException.class, () -> courseServiceImpl.patchCourse(mockId, mokCourse));
//    }

//    @Test
//    void deleteCourseVerifyMethod() {
//        MockitoAnnotations.openMocks(this);
//        //given
//        String mockId = "1111";
//        Course mockCourse = prepareCourse();
//        mockCourse.setId(mockId);
//        given(courseRepository.findById(mockId)).willReturn(Optional.of(mockCourse));
//        //when
//        courseServiceImpl.deleteCourseById(mockId);
//        //then
//        verify(courseRepository, times(1)).findById(mockId);
//        verify(courseRepository, times(1)).deleteById(mockId);
//        verify(calendarServiceClient, times(1)).deleteCourseLessons(mockId);
//
//    }

//    @Test
//    void deleteCourseShouldBeReturnCourseNotFound() {
//        MockitoAnnotations.openMocks(this);
//        //given
//        String mockId = "1111";
//        Course mockCourse = prepareCourse();
//        mockCourse.setId(mockId);
//        CourseException mockException = new CourseException(CourseError.COURSE_NOT_FOUND);
//        given(courseRepository.findById(mockId)).willThrow(mockException);
//        //when
//        //then
//        assertThrows(CourseException.class, () -> courseServiceImpl.deleteCourseById(mockId));
//    }






}