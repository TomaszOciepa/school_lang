package com.tom.studentservice.controller;

import com.tom.studentservice.exception.StudentError;
import com.tom.studentservice.exception.StudentException;
import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import com.tom.studentservice.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private List<Student> prepareStudentsData() {
        List<Student> mockStudents = Arrays.asList(
                new Student(1L, "Tomasz", "Kowalski", "tom@wp.pl", Status.ACTIVE, ""),
                new Student(2L, "Mango", "Nowak", "mangi@wp.pl", Status.INACTIVE, "")
        );
        return mockStudents;
    }

    private Student prepareStudent() {
        Student mockStudent = new Student(1L, "Jan", "Nowak", "jan@wp.pl", Status.ACTIVE,"");
        return mockStudent;
    }

    @Test
    void getAllStudents() {
        //given
        MockitoAnnotations.openMocks(this);
        List<Student> mockStudents = prepareStudentsData();
        Status mockStatus = Status.ACTIVE;

        given(studentService.getStudents(mockStatus)).willReturn(mockStudents);
        //when
        List<Student> result = studentController.getStudents(mockStatus);
        //then
        assertEquals(mockStudents, result);
    }


    @Test
    void getStudentByIdShouldBeReturnStudent() {
        MockitoAnnotations.openMocks(this);
        //given

        Student mockStudent = prepareStudent();
        given(studentService.getStudentById(1L)).willReturn(mockStudent);
        //when
        Student result = studentController.getStudentById(1L);

        //then
        assertEquals(mockStudent, result);
    }


    @Test
    void getStudentByIdShouldBeReturnExceptionStudentNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        StudentException studentException = new StudentException(StudentError.STUDENT_NOT_FOUND);

        Long mockStudentId = 1L;
        given(studentService.getStudentById(mockStudentId)).willThrow(studentException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentController.getStudentById(mockStudentId));
    }

    @Test
    void getStudentByIdShouldBeReturnExceptionStudentIsNotActive() {
        MockitoAnnotations.openMocks(this);
        //given
        StudentException studentException = new StudentException(StudentError.STUDENT_IS_NOT_ACTIVE);

        Long mockStudentId = 1L;
        given(studentService.getStudentById(mockStudentId)).willThrow(studentException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentController.getStudentById(mockStudentId));
    }

    @Test
    void getStudentByEmailShouldBeReturnStudent() {
        MockitoAnnotations.openMocks(this);
        //given
        Student mockStudent = prepareStudent();
        String email = "mangi@wp.pl";
        given(studentService.getStudentByEmail(email)).willReturn(mockStudent);

        //when
        Student result = studentController.getStudentByEmail(email);

        //then
        assertEquals(mockStudent, result);
    }

    @Test
    void getStudentByEmailShouldBeReturnExceptionStudentNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        StudentException studentException = new StudentException(StudentError.STUDENT_NOT_FOUND);
        String email = "mangi@wp.pl";
        given(studentService.getStudentByEmail(email)).willThrow(studentException);

        //when
        //then
        assertThrows(StudentException.class, () -> studentController.getStudentByEmail(email));
    }

    @Test
    void getStudentByEmailShouldBeReturnExceptionStudentIsNotActive() {
        MockitoAnnotations.openMocks(this);
        //given
        StudentException studentException = new StudentException(StudentError.STUDENT_IS_NOT_ACTIVE);
        String email = "mangi@wp.pl";
        given(studentService.getStudentByEmail(email)).willThrow(studentException);

        //when
        //then
        assertThrows(StudentException.class, () -> studentController.getStudentByEmail(email));
    }

    @Test
    void addStudentShouldBeReturnNewStudent() {
        MockitoAnnotations.openMocks(this);
        //given
        Student mockStudent = prepareStudent();
        given(studentService.addStudent(mockStudent)).willReturn(mockStudent);
        //when
        Student result = studentController.addStudent(mockStudent);

        //then
        assertEquals(mockStudent, result);
    }

    @Test
    void addStudentShouldBeReturnExceptionEmailAlreadyExists() {
        MockitoAnnotations.openMocks(this);
        //given
        Student mockStudent = prepareStudent();
        StudentException studentException = new StudentException(StudentError.STUDENT_EMAIL_ALREADY_EXISTS);
        given(studentService.addStudent(mockStudent)).willThrow(studentException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentController.addStudent(mockStudent));
    }



    @Test
    void patchStudentShouldBeReturnExceptionStudentNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Student mockStudent = prepareStudent();
        Long mockStudentId = 1L;
        StudentException studentException = new StudentException(StudentError.STUDENT_NOT_FOUND);
        given(studentService.patchStudent(mockStudentId, mockStudent)).willThrow(studentException);
        //when
        //then
        assertThrows(StudentException.class, () -> studentController.patchStudent(mockStudentId, mockStudent));
    }

    @Test
    void deleteStudentVerifyMethod() {
        MockitoAnnotations.openMocks(this);
        //given
        willDoNothing().given(studentService).deactivateStudentById(1L);
        //when
        studentController.deactivateStudentById(1L);
        //then
        verify(studentService).deactivateStudentById(1L);
    }

    @Test
    void getStudentsByEmails() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Student> mockStudentList = prepareStudentsData();
        List<String> mockStudentsEmailsList= Arrays.asList("tom@wp.pl", "mangi@wp.pl");
        given(studentService.getStudentsByEmails(mockStudentsEmailsList)).willReturn(mockStudentList);
        //when
        List<Student> result = studentController.getStudentsByEmails(mockStudentsEmailsList);
        //then
        assertEquals(mockStudentList, result);
    }

    @Test
    void getStudentsByIdNumber() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Student> mockStudentList = prepareStudentsData();
        List<Long> mockStudentsIdNumberList= Arrays.asList(1L, 2L);
        given(studentService.getStudentsByIdNumbers(mockStudentsIdNumberList)).willReturn(mockStudentList);
        //when
        List<Student> result = studentController.getStudentsByIdNumbers(mockStudentsIdNumberList);
        //then
        assertEquals(mockStudentList, result);
    }
}