package com.tom.teacherservice.controller;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import com.tom.teacherservice.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private TeacherController teacherController;

    private List<Teacher> prepareTeachersData() {
        List<Teacher> mockTeachers = Arrays.asList(
                new Teacher(1L, "Robert", "Kubica", "rob@wp.pl", Status.ACTIVE, new ArrayList<>()),
                new Teacher(2L, "Krzysztof", "Hołowczyc", "hol@wp.pl", Status.INACTIVE, new ArrayList<>())
        );
        return mockTeachers;
    }

    private Teacher prepareTeacher() {
        Teacher mockStudent = new Teacher(1L, "Robert", "Kubica", "rob@wp.pl", Status.ACTIVE, new ArrayList<>());
        return mockStudent;
    }

    @Test
    void getAllTeacherWithStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Teacher> mockTeacherList = prepareTeachersData();
        Status mockStatus = Status.ACTIVE;
        given(teacherService.getAllTeacher(mockStatus)).willReturn(mockTeacherList);
        //when
        List<Teacher> result = teacherController.getAllTeacher(mockStatus);
        //then
        assertEquals(mockTeacherList, result);
    }

    @Test
    void getAllTeacherWithoutStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Teacher> mockTeacherList = prepareTeachersData();

        given(teacherService.getAllTeacher(null)).willReturn(mockTeacherList);
        //when
        List<Teacher> result = teacherController.getAllTeacher(null);
        //then
        assertEquals(mockTeacherList, result);
    }

    @Test
    void getTeacherByIdShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        Long mocId = 1L;
        given(teacherService.getTeacherById(mocId)).willReturn(mockTeacher);
        //when
        Teacher result = teacherController.getTeacherById(mocId);
        //then
        assertEquals(mockTeacher, result);
    }

    @Test
    void getTeacherByEmailShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        String mockEmail = mockTeacher.getEmail();
        given(teacherService.getTeacherByEmail(mockEmail)).willReturn(mockTeacher);
        //when
        Teacher result = teacherController.getTeacherByEmail(mockEmail);
        //then
        assertEquals(mockTeacher, result);
    }

    @Test
    void addTeacherShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        given(teacherService.addTeacher(mockTeacher)).willReturn(mockTeacher);
        //when
        Teacher result = teacherController.addTeacher(mockTeacher);
        //then
        assertEquals(mockTeacher, result);
    }

    @Test
    void putTeacherShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        Long mockId = mockTeacher.getId();
        given(teacherService.putTeacher(mockId, mockTeacher)).willReturn(mockTeacher);
        //when
        Teacher result = teacherController.putTeacher(mockId, mockTeacher);
        //then
        assertEquals(mockTeacher, result);
    }

    @Test
    void patchTeacherShouldBeReturnTeacher() {
        MockitoAnnotations.openMocks(this);
        //given
        Teacher mockTeacher = prepareTeacher();
        Long mockId = mockTeacher.getId();
        given(teacherService.patchTeacher(mockId, mockTeacher)).willReturn(mockTeacher);
        //when
        Teacher result = teacherController.patchTeacher(mockId, mockTeacher);
        //then
        assertEquals(mockTeacher, result);
    }

    @Test
    void deleteTeacherVerifyMethod() {
        MockitoAnnotations.openMocks(this);
        //given
        Long teacherId = 1L;
        willDoNothing().given(teacherService).deleteTeacher(teacherId);
        //when
        teacherController.deleteTeacher(teacherId);
        //then
        verify(teacherService, times(1)).deleteTeacher(teacherId);
    }
}