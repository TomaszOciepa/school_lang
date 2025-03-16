package com.tom.teacherservice.service;

import com.tom.teacherservice.exception.TeacherError;
import com.tom.teacherservice.exception.TeacherException;
import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import com.tom.teacherservice.repo.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherServiceImpl teacherServiceImpl;

    private List<Teacher> prepareTeachersData() {
        List<Teacher> mockTeachers = Arrays.asList(
                new Teacher(1L, "Robert", "Kubica", "rob@wp.pl", Status.ACTIVE, "pass"),
                new Teacher(2L, "Krzysztof", "Hołowczyc", "hol@wp.pl", Status.INACTIVE, "pass")
        );
        return mockTeachers;
    }

    private Teacher prepareTeacher() {
        Teacher mockStudent = new Teacher(1L, "Robert", "Kubica", "rob@wp.pl", Status.ACTIVE, "pass");
        return mockStudent;
    }

    @Test
    void getAllTeacherWithStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Teacher> mockTeacherList = prepareTeachersData();
        Status mockStatus = Status.ACTIVE;
        given(teacherRepository.findAllByStatus(mockStatus)).willReturn(mockTeacherList);
        //when
        List<Teacher> result = teacherServiceImpl.getTeachers(mockStatus);
        //then
        assertEquals(mockTeacherList, result);
    }

    @Test
    void getAllTeacherWithoutStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Teacher> mockTeacherList = prepareTeachersData();
        given(teacherRepository.findAll()).willReturn(mockTeacherList);
        //when
        List<Teacher> result = teacherServiceImpl.getTeachers(null);
        //then
        assertEquals(mockTeacherList, result);
    }

    @Test
    void getTeacherByEmailShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        String mockEmail = mockTeacher.getEmail();
        given(teacherRepository.findByEmail(mockEmail)).willReturn(Optional.of(mockTeacher));
        //when
        Teacher results = teacherServiceImpl.getTeacherByEmail(mockEmail);
        //then
        assertEquals(mockTeacher, results);
    }

    @Test
    void getTeacherByEmailShouldBeReturnTeacherIsNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockEmail = "rob@wp.pl";
        TeacherException mockException = new TeacherException(TeacherError.TEACHER_NOT_FOUND);
        given(teacherRepository.findByEmail(mockEmail)).willThrow(mockException);
        //when
        //then
        assertThrows(TeacherException.class, () -> teacherServiceImpl.getTeacherByEmail(mockEmail));
    }

    @Test
    void getTeacherByEmailShouldBeReturnTeacherIsNotActive() {
        MockitoAnnotations.openMocks(this);
        //given
        String mockEmail = "rob@wp.pl";
        TeacherException mockException = new TeacherException(TeacherError.TEACHER_IS_NOT_ACTIVE);
        given(teacherRepository.findByEmail(mockEmail)).willThrow(mockException);
        //when
        //then
        assertThrows(TeacherException.class, () -> teacherServiceImpl.getTeacherByEmail(mockEmail));
    }

    @Test
    void addStudentShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        given(teacherRepository.save(mockTeacher)).willReturn(mockTeacher);
        //when
        Teacher result = teacherServiceImpl.addTeacher(mockTeacher);
        //then
        assertEquals(mockTeacher, result);
    }

    @Test
    void addStudentShouldBeReturnExceptionTeacherEmailIsAlreadyExists() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        TeacherException mockException = new TeacherException(TeacherError.TEACHER_EMAIL_ALREADY_EXISTS);
        given(teacherRepository.save(mockTeacher)).willThrow(mockException);
        //when
        //then
        assertThrows(TeacherException.class, () -> teacherServiceImpl.addTeacher(mockTeacher));
    }

    @Test
    void putTeacherShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        Long mockId = 1L;
        given(teacherRepository.findById(mockId)).willReturn(Optional.of(mockTeacher));
        given(teacherRepository.existsByEmail(mockTeacher.getEmail())).willReturn(false);
        given(teacherRepository.save(mockTeacher)).willReturn(mockTeacher);

        //when
        Teacher result = teacherServiceImpl.putTeacher(mockId, mockTeacher);
        //then
        assertEquals(mockTeacher, result);
    }

    @Test
    void putTeacherShouldBeReturnExceptionTeacherNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        Long mockId = 1L;
        TeacherException mockException = new TeacherException(TeacherError.TEACHER_NOT_FOUND);
        given(teacherRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(TeacherException.class, () -> teacherServiceImpl.putTeacher(mockId, mockTeacher));
    }

    @Test
    void putTeacherShouldBeReturnExceptionTeacherEmailIsAlreadyExists() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        Long mockId = 1L;
        TeacherException mockException = new TeacherException(TeacherError.TEACHER_EMAIL_ALREADY_EXISTS);
        given(teacherRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(TeacherException.class, () -> teacherServiceImpl.putTeacher(mockId, mockTeacher));
    }


    @Test
    void patchTeacherShouldBeReturnExceptionTeacherNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        Long mockId = 1L;
        TeacherException mockException = new TeacherException(TeacherError.TEACHER_NOT_FOUND);
        given(teacherRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(TeacherException.class, ()->teacherServiceImpl.patchTeacher(mockId, mockTeacher));
    }


    @Test
    void getTeachersByIdNumber() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Teacher> mockTeacherList = prepareTeachersData();
        List<Long> mockTeachersIdNumberList = Arrays.asList(1L, 2L);
        given(teacherRepository.findAllByIdIn(mockTeachersIdNumberList)).willReturn(mockTeacherList);
        //when
        List<Teacher> result = teacherServiceImpl.getTeachersByIdNumber(mockTeachersIdNumberList);
        //then
        assertEquals(mockTeacherList, result);
    }

}