package com.tom.adminservice.service;

import com.tom.adminservice.exception.AdminError;
import com.tom.adminservice.exception.AdminException;
import com.tom.adminservice.model.Admin;
import com.tom.adminservice.model.Status;
import com.tom.adminservice.repo.AdminRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    List<Admin> prepareAdminData() {
        return Arrays.asList(new Admin(1L, "Michał", "Pol", "pol@wp.pl", Status.ACTIVE),
                new Admin(1L, "Krzysztof", "Stanowski", "stano@wp.pl", Status.ACTIVE));
    }

    Admin prepareAdmin() {
        return new Admin(1L, "Michał", "Pol", "pol@wp.pl", Status.ACTIVE);
    }

    @Test
    void getAllAdminWithStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Admin> mockAdminList = prepareAdminData();
        Status mockStatus = Status.ACTIVE;
        given(adminRepository.findAllByStatus(mockStatus)).willReturn(mockAdminList);
        //when
        List<Admin> result = adminServiceImpl.getAllAdmin(mockStatus);
        //then
        assertEquals(mockAdminList, result);
    }

    @Test
    void getAllAdminWithoutStatus() {
        MockitoAnnotations.openMocks(this);
        //given
        List<Admin> mockAdminList = prepareAdminData();
        given(adminRepository.findAll()).willReturn(mockAdminList);
        //when
        List<Admin> result = adminServiceImpl.getAllAdmin(null);
        //then
        assertEquals(mockAdminList, result);
    }

    @Test
    void getAdminByIdShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = 1L;
        given(adminRepository.findById(mockId)).willReturn(Optional.ofNullable(mockAdmin));
        //when
        Admin result = adminServiceImpl.getAdminById(mockId);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void getAdminByIdShouldBeReturnExceptionAdminNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Long mockId = 1L;
        AdminException mockException = new AdminException(AdminError.ADMIN_NOT_FOUND);
        given(adminRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, () -> adminServiceImpl.getAdminById(mockId));
    }

    @Test
    void getAdminByIdShouldBeReturnExceptionAdminIsNotActive() {
        MockitoAnnotations.openMocks(this);
        //given
        Long mockId = 1L;
        AdminException mockException = new AdminException(AdminError.ADMIN_IS_NOT_ACTIVE);
        given(adminRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, () -> adminServiceImpl.getAdminById(mockId));
    }

    @Test
    void getAdminByEmailShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        String mocEmail = mockAdmin.getEmail();
        given(adminRepository.findByEmail(mocEmail)).willReturn(Optional.of(mockAdmin));
        //when
        Admin result = adminServiceImpl.getAdminByEmail(mocEmail);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void getAdminByEmailShouldBeReturnExceptionAdminNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        String mocEmail = "stano@wp.pl";
        AdminException mockException = new AdminException(AdminError.ADMIN_NOT_FOUND);
        given(adminRepository.findByEmail(mocEmail)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, () -> adminServiceImpl.getAdminByEmail(mocEmail));
    }

    @Test
    void getAdminByEmailShouldBeReturnExceptionAdminIsNotActive() {
        MockitoAnnotations.openMocks(this);
        //given
        String mocEmail = "stano@wp.pl";
        AdminException mockException = new AdminException(AdminError.ADMIN_IS_NOT_ACTIVE);
        given(adminRepository.findByEmail(mocEmail)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, () -> adminServiceImpl.getAdminByEmail(mocEmail));
    }

    @Test
    void addAdminShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        given(adminRepository.save(mockAdmin)).willReturn(mockAdmin);
        //when
        Admin result = adminServiceImpl.addAdmin(mockAdmin);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void addAdminShouldBeReturnExceptionAdminEmailAlreadyExists() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        AdminException mockException = new AdminException(AdminError.ADMIN_EMAIL_ALREADY_EXISTS);
        given(adminRepository.save(mockAdmin)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, () -> adminServiceImpl.addAdmin(mockAdmin));
    }

    @Test
    void putAdminShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = 1L;
        given(adminRepository.findById(mockId)).willReturn(Optional.ofNullable(mockAdmin));
        given(adminRepository.save(mockAdmin)).willReturn(mockAdmin);
        //when
        Admin result = adminServiceImpl.putAdmin(mockId, mockAdmin);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void putAdminShouldBeReturnExceptionAdminNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = 1L;
        AdminException mockException = new AdminException(AdminError.ADMIN_NOT_FOUND);
        given(adminRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, () -> adminServiceImpl.putAdmin(mockId, mockAdmin));
    }

    @Test
    void putAdminShouldBeReturnExceptionAdminEmailAlreadyExists() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = 1L;
        AdminException mockException = new AdminException(AdminError.ADMIN_EMAIL_ALREADY_EXISTS);
        given(adminRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, () -> adminServiceImpl.putAdmin(mockId, mockAdmin));
    }

    @Test
    void patchAdminShouldBeReturnAdmin() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = 1L;
        given(adminRepository.findById(mockId)).willReturn(Optional.ofNullable(mockAdmin));
        given(adminRepository.save(mockAdmin)).willReturn(mockAdmin);
        //when
        Admin result = adminServiceImpl.patchAdmin(mockId, mockAdmin);
        //then
        assertEquals(mockAdmin, result);
    }

    @Test
    void patchAdminShouldBeReturnExceptionAdminNotFound() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = 1L;
        AdminException mockException = new AdminException(AdminError.ADMIN_NOT_FOUND);
        given(adminRepository.findById(mockId)).willThrow(mockException);
        //when
        //then
        assertThrows(AdminException.class, ()->adminServiceImpl.patchAdmin(mockId, mockAdmin));
    }

    @Test
    void deleteAdminVerifyMethod() {
        MockitoAnnotations.openMocks(this);
        //given
        Admin mockAdmin = prepareAdmin();
        Long mockId = mockAdmin.getId();
        given(adminRepository.findById(mockId)).willReturn(Optional.of(mockAdmin));
        given(adminRepository.save(mockAdmin)).willReturn(mockAdmin);
        //when
        adminServiceImpl.deleteAdmin(mockId);
        //then
        verify(adminRepository, times(1)).findById(mockId);
        verify(adminRepository, times(1)).save(mockAdmin);
        assertEquals(Status.INACTIVE, mockAdmin.getStatus());
    }
}