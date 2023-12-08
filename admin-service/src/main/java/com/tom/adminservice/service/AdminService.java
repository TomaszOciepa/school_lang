package com.tom.adminservice.service;


import com.tom.adminservice.model.Admin;
import com.tom.adminservice.model.Status;

import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmin(Status status);

    Admin getAdminById(Long id);

    Admin getAdminByEmail(String email);

    Admin addAdmin(Admin admin);

    Admin putAdmin(Long id, Admin admin);

    Admin patchAdmin(Long id, Admin admin);

    void deleteAdmin(Long id);

}
