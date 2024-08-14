package com.tom.studentservice.service;

import com.tom.studentservice.exception.StudentError;
import com.tom.studentservice.exception.StudentException;
import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    private List<Student> prepareStudentsData() {
        List<Student> mockStudents = Arrays.asList(
                new Student(1L, "Tomasz", "Kowalski", "tom@wp.pl", Status.ACTIVE),
                new Student(2L, "Mango", "Nowak", "mangi@wp.pl", Status.INACTIVE)
        );
        return mockStudents;
    }

    private Student prepareStudent() {
        Student mockStudent = new Student(1L, "Jan", "Nowak", "jan@wp.pl", Status.ACTIVE);
        return mockStudent;
    }

    @Test
    void getAllStudentsWithStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Student> mockStudentsList = prepareStudentsData();
        Status mockStatus = Status.ACTIVE;
        given(studentRepository.findAllByStatus(Status.ACTIVE)).willReturn(mockStudentsList);

        //when
        List<Student> result = studentServiceImpl.getStudents(mockStatus);
        //then
        assertEquals(mockStudentsList, result);
    }

    @Test
    void getAllStudentsWithoutStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Student> mockStudentsList = prepareStudentsData();
        given(studentRepository.findAll()).willReturn(mockStudentsList);

        //when
        List<Student> result = studentServiceImpl.getStudents(null);
        //then
        assertEquals(mockStudentsList, result);
    }

    @Test
    void getStudentByEmailShouldBeReturnStudent() {
        MockitoAnnotations.openMocks(this);
        //given
        Student mockStudent = prepareStudent();
        String mockEmail = "mangi@wp.pl";
        given(studentRepository.findByEmail(mockEmail)).willReturn(Optional.of(mockStudent));
        //when
        Student result = studentServiceImpl.getStudentByEmail(mockEmail);
        //then
        assertEquals(mockStudent, result);
    }

    @Test
    void getStudentByEmailShouldBeReturnExceptionStudentIsNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        StudentException mockException = new StudentException(StudentError.STUDENT_NOT_FOUND);
        String mockEmail = "mangi@wp.pl";
        given(studentRepository.findByEmail(mockEmail)).willThrow(mockException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentServiceImpl.getStudentByEmail(mockEmail));
    }

    @Test
    void getStudentByEmailShouldBeReturnExceptionStudentIsNotActive() {
        MockitoAnnotations.openMocks(this);
        //given
        StudentException mockException = new StudentException(StudentError.STUDENT_IS_NOT_ACTIVE);
        String mockEmail = "mangi@wp.pl";
        given(studentRepository.findByEmail(mockEmail)).willThrow(mockException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentServiceImpl.getStudentByEmail(mockEmail));
    }

//    @Test
//    void patchStudentShouldBEReturnStudent() {
//        MockitoAnnotations.openMocks(this);
//        //given
//        Student mockStudent = prepareStudent();
//        Long mockId = 1L;
//        given(studentRepository.findById(mockId)).willReturn(Optional.of(mockStudent));
//        given(studentRepository.save(mockStudent)).willReturn(mockStudent);
//        //when
//        Student result = studentServiceImpl.patchStudent(mockId, mockStudent);
//        //then
//        assertEquals(mockStudent, result);
//    }

    @Test
    void patchStudentShouldBEReturnStudentNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Student mockStudent = prepareStudent();
        Long mockId = 1L;
        StudentException mockException = new StudentException(StudentError.STUDENT_NOT_FOUND);
        given(studentRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentServiceImpl.patchStudent(mockId, mockStudent));
    }

    @Test
    void deleteStudentShouldBeReturnStudentNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Long mockId = 1L;
        StudentException mockException = new StudentException(StudentError.STUDENT_NOT_FOUND);
        given(studentRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentServiceImpl.deactivateStudentById(mockId));
    }

    @Test
    void getStudentsByEmails() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Student> mockStudentList = prepareStudentsData();
        List<String> mockStudentsEmailsList = Arrays.asList("tom@wp.pl", "mangi@wp.pl");
        given(studentRepository.findAllByEmailIn(mockStudentsEmailsList)).willReturn(mockStudentList);
        //when
        List<Student> result = studentServiceImpl.getStudentsByEmails(mockStudentsEmailsList);
        //then
        assertEquals(mockStudentList, result);
    }

    @Test
    void getStudentsByIdNumber() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Student> mockStudentList = prepareStudentsData();
        List<Long> mockStudentsIdNumberList = Arrays.asList(1L, 2L);
        given(studentRepository.findAllByIdIn(mockStudentsIdNumberList)).willReturn(mockStudentList);
        //when
        List<Student> result = studentServiceImpl.getStudentsByIdNumbers(mockStudentsIdNumberList);
        //then
        assertEquals(mockStudentList, result);
    }
}