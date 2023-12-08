package com.tom.adminservice.controller;

import com.tom.adminservice.model.Admin;
import com.tom.adminservice.model.Status;
import com.tom.adminservice.service.AdminService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AdminControllerTest {

    @Mock
    private AdminService adminService;
    @InjectMocks
    private AdminController adminController;

    List<Admin> prepareAdminData() {
        return Arrays.asList(new Admin(1L, "Michał", "Pol", "pol@wp.pl", Status.ACTIVE),
                new Admin(1L, "Krzysztof", "Stanowski", "stano@wp.pl", Status.ACTIVE));
    }

    Admin prepareAdmin() {
        return new Admin(1L, "Michał", "Pol", "pol@wp.pl", Status.ACTIVE);
    }

    @Test
    void getAllAdminsWithStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Admin> mockAdminList = prepareAdminData();
        Status mockStatus = Status.ACTIVE;
        given(adminService.getAllAdmin(mockStatus)).willReturn(mockAdminList);
        //when
        List<Admin> result = adminController.getAllAdmins(mockStatus);
        //then
        assertEquals(mockAdminList, result);
    }

    @Test
    void getAllAdminsWithoutStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Admin> mockAdminList = prepareAdminData();
        given(adminService.getAllAdmin(null)).willReturn(mockAdminList);
        //when
        List<Admin> result = adminController.getAllAdmins(null);
        //then
        assertEquals(mockAdminList, result);
    }

    @Test
    void getAdminByIdShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = 1L;
        given(adminService.getAdminById(mockId)).willReturn(mockAdmin);
        //when
        Admin result = adminController.getAdminById(mockId);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void getAdminByEmailShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        String mockEmail = mockAdmin.getEmail();
        given(adminService.getAdminByEmail(mockEmail)).willReturn(mockAdmin);
        //when
        Admin result = adminController.getAdminByEmail(mockEmail);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void addAdminShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        given(adminService.addAdmin(mockAdmin)).willReturn(mockAdmin);
        //when
        Admin result = adminController.addAdmin(mockAdmin);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void putAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = mockAdmin.getId();
        given(adminService.putAdmin(mockId, mockAdmin)).willReturn(mockAdmin);
        //when
        Admin result = adminController.putAdmin(mockId, mockAdmin);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void patchAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = mockAdmin.getId();
        given(adminService.patchAdmin(mockId, mockAdmin)).willReturn(mockAdmin);
        //when
        Admin result = adminController.patchAdmin(mockId, mockAdmin);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void deleteAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Long mockId = 1L;
        willDoNothing().given(adminService).deleteAdmin(mockId);
        //when
        adminController.deleteAdmin(mockId);
        //then
        verify(adminService, times(1)).deleteAdmin(mockId);
    }
}