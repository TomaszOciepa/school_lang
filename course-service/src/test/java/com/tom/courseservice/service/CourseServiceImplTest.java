package com.tom.courseservice.service;

import com.tom.courseservice.exception.CourseError;
import com.tom.courseservice.exception.CourseException;
import com.tom.courseservice.model.*;
import com.tom.courseservice.model.dto.CourseStudentDto;
import com.tom.courseservice.model.dto.StudentDto;
import com.tom.courseservice.repo.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;


    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentServiceClient studentServiceClient;

    private Course courseMock1;
    private Course courseMock2;

    @BeforeEach
    void setUp() {
            courseMock1 = createCourse("212", Status.ACTIVE, "kurs polskiego", LocalDateTime.now(), LocalDateTime.now().plusWeeks(2));
            courseMock2 = createCourse("213", Status.INACTIVE, "kurs angielskiego", LocalDateTime.now().minusMonths(1), LocalDateTime.now().minusWeeks(1));
        }

    @Test
    void getAllByStatus_ShouldReturnCoursesWithGivenStatus_WhenStatusIsNotNull() {
        // Given
        Status status = Status.ACTIVE;

        List<Course> mockCourses =  Arrays.asList(courseMock1, courseMock2);


        when(courseRepository.findByStatus(eq(status), any(Sort.class))).thenReturn(mockCourses);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // When
        List<Course> result = courseService.getAllByStatus(status);


        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(Status.ACTIVE, result.get(0).getStatus());

        verify(courseRepository, times(1)).findByStatus(eq(status), any(Sort.class));
        verify(courseRepository, never()).findAllByOrderByStartDateAsc();
    }

    @Test
    void getAllByStatus_ShouldReturnAllCourses_WhenStatusIsNull() {
        // Given
        Status status = null;
        List<Course> mockCourses = Arrays.asList(courseMock1, courseMock2);

        when(courseRepository.findAllByOrderByStartDateAsc()).thenReturn(mockCourses);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Course> result = courseService.getAllByStatus(status);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(Status.ACTIVE, result.get(0).getStatus());
        Assertions.assertEquals(Status.FINISHED, result.get(1).getStatus());

        verify(courseRepository, times(1)).findAllByOrderByStartDateAsc();
        verify(courseRepository, never()).findByStatus(any(), any());
    }

    @Test
    void getAllByStatus_ShouldReturnEmptyList_WhenNoCoursesAvailable() {
        // Given
        Status status = Status.ACTIVE;
        List<Course> mockCourses = new ArrayList<>();

        when(courseRepository.findByStatus(eq(status), any(Sort.class))).thenReturn(mockCourses);

        // When
        List<Course> result = courseService.getAllByStatus(status);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        verify(courseRepository, times(1)).findByStatus(eq(status), any(Sort.class));
        verify(courseRepository, never()).findAllByOrderByStartDateAsc();
    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenStatusIsNull() {
        // Given
        String courseId = "212";
        Status status = null;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseMock1));
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // When
        Course result = courseService.getCourseById(courseId, status);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(courseId, result.getId());
        Assertions.assertEquals(Status.ACTIVE, result.getStatus());

        verify(courseRepository, times(1)).findById(courseId);
    }


    @Test
    void getCourseById_ShouldReturnCourse_WhenStatusIsNotNullAndCourseIsFound() {
        // Given
        String courseId = "212";
        Status status = Status.ACTIVE;

        when(courseRepository.findByIdAndStatus(courseId, status)).thenReturn(Optional.of(courseMock1));
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // When
        Course result = courseService.getCourseById(courseId, status);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(courseId, result.getId());
        Assertions.assertEquals(Status.ACTIVE, result.getStatus());

        verify(courseRepository, times(1)).findByIdAndStatus(courseId, status);
    }

    @Test
    void getCourseById_ShouldThrowCourseException_WhenCourseNotFoundWithStatus() {
        // Given
        String courseId = "212";
        Status status = Status.ACTIVE;

        when(courseRepository.findByIdAndStatus(courseId, status)).thenReturn(Optional.empty());

        // When & Then
        CourseException exception = Assertions.assertThrows(CourseException.class, () -> {
            courseService.getCourseById(courseId, status);
        });

        Assertions.assertEquals(CourseError.COURSE_NOT_FOUND, exception.getCourseError());

        verify(courseRepository, times(1)).findByIdAndStatus(courseId, status);
    }

    @Test
    void getCourseById_ShouldThrowCourseException_WhenCourseNotFoundWithoutStatus() {
        // Given
        String courseId = "212";
        Status status = null;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        CourseException exception = Assertions.assertThrows(CourseException.class, () -> {
            courseService.getCourseById(courseId, status);
        });

        Assertions.assertEquals(CourseError.COURSE_NOT_FOUND, exception.getCourseError());

        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void getCourseById_ShouldThrowCourseException_WhenStatusIsNotNullAndCourseNotFound() {
        // Given
        String courseId = "212";
        Status status = Status.INACTIVE;

        when(courseRepository.findByIdAndStatus(courseId, status)).thenReturn(Optional.empty());

        // When & Then
        CourseException exception = Assertions.assertThrows(CourseException.class, () -> {
            courseService.getCourseById(courseId, status);
        });

        Assertions.assertEquals(CourseError.COURSE_NOT_FOUND, exception.getCourseError());

        verify(courseRepository, times(1)).findByIdAndStatus(courseId, status);
    }

    @Test
    void getCoursesByLanguage_ShouldReturnCourses_WhenCoursesExist() {
        // Given
        Language language = Language.POLISH;
        List<Course> mockCourses = Arrays.asList(courseMock1, courseMock2);

        when(courseRepository.getCoursesByLanguage(language, Status.INACTIVE)).thenReturn(mockCourses);

        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Course> result = courseService.getCoursesByLanguage(language);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(language, result.get(0).getLanguage());

        verify(courseRepository, times(1)).getCoursesByLanguage(language, Status.INACTIVE);
    }

    @Test
    void getCoursesByLanguage_ShouldThrowCourseException_WhenNoCoursesFound() {
        // Given
        Language language = Language.POLISH;

        when(courseRepository.getCoursesByLanguage(language, Status.INACTIVE)).thenReturn(Collections.emptyList());

        // When & Then
        CourseException exception = Assertions.assertThrows(CourseException.class, () -> {
            courseService.getCoursesByLanguage(language);
        });

        Assertions.assertEquals(CourseError.COURSE_NOT_FOUND, exception.getCourseError());

        verify(courseRepository, times(1)).getCoursesByLanguage(language, Status.INACTIVE);
    }

    @Test
    void getCourseTotalAmount_ShouldReturnCoursePrice_WhenCourseExists() {
        // Given
        String courseId = "212";
        Long expectedCoursePrice = 1000L;

        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseMock1));
        when(courseService.getCourseById(courseId, null)).thenReturn(courseMock1);

        // When
        String result = courseService.getCourseTotalAmount(courseId);

        // Then
        Assertions.assertNotNull(result, "Result should not be null");
        Assertions.assertEquals(expectedCoursePrice.toString(), result, "Course price should match the expected value");
    }





    private Course createCourse(String id, Status status, String name, LocalDateTime startDate, LocalDateTime endDate){
    Course course = new Course();
    course.setId(id);
    course.setName(name);
    course.setStatus(status);
    course.setCoursePrice(1000L);
    course.setPricePerLesson(100L);
    course.setTeacherSharePercentage(50L);
    course.setLanguage(Language.POLISH);
    course.setParticipantsLimit(12L);
    course.setParticipantsNumber(10L);
    course.setLessonsLimit(10L);
    course.setStartDate(startDate);
    course.setEndDate(endDate);
    course.setTimeRange(TimeRange.AFTERNOON);
    course.setLessonDuration(60L);
    course.setTeacherId(1L);
    course.setLessonFrequency(LessonFrequency.DAILY);
    return course;
    }


    private StudentDto createStudentDto(Long id) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(id);
        studentDto.setFirstName("John"+id);
        studentDto.setLastName("Rambo"+id);
        studentDto.setStatus(Status.ACTIVE);
        studentDto.setEmail("rambo"+id+"@buziaczek.pl");
        studentDto.setPassword("password"+id);
        return studentDto;
    }
}