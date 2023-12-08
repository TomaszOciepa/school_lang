package com.tom.adminservice.service;

import com.tom.adminservice.exception.AdminError;
import com.tom.adminservice.exception.AdminException;
import com.tom.adminservice.model.Admin;
import com.tom.adminservice.model.Status;
import com.tom.adminservice.repo.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<Admin> getAllAdmin(Status status) {

        if (status != null) {
            return adminRepository.findAllByStatus(status);
        }
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminById(Long id) {

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminException(AdminError.ADMIN_NOT_FOUND));
        if (!Status.ACTIVE.equals(admin.getStatus())) {
            throw new AdminException(AdminError.ADMIN_IS_NOT_ACTIVE);
        }
        return admin;
    }

    @Override
    public Admin getAdminByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminException(AdminError.ADMIN_NOT_FOUND));
        if (!Status.ACTIVE.equals(admin.getStatus())) {
            throw new AdminException(AdminError.ADMIN_IS_NOT_ACTIVE);
        }
        return admin;
    }

    @Override
    public Admin addAdmin(Admin admin) {
        validateAdminEmailExists(admin.getEmail());
        admin.setStatus(Status.ACTIVE);
        return adminRepository.save(admin);
    }

    private void validateAdminEmailExists(String email) {
        if (adminRepository.existsByEmail(email)) {
            throw new AdminException(AdminError.ADMIN_EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public Admin putAdmin(Long id, Admin admin) {
        return adminRepository.findById(id)
                .map(adminFromDb -> {
                            if (!adminFromDb.getEmail().equals(admin.getEmail())
                                    && adminRepository.existsByEmail(admin.getEmail())) {
                                throw new AdminException(AdminError.ADMIN_EMAIL_ALREADY_EXISTS);
                            }
                            adminFromDb.setFirstName(admin.getFirstName());
                            adminFromDb.setLastName(admin.getLastName());
                            adminFromDb.setStatus(admin.getStatus());
                            return adminRepository.save(adminFromDb);
                        }
                )
                .orElseThrow(() -> new AdminException(AdminError.ADMIN_NOT_FOUND));
    }

    @Override
    public Admin patchAdmin(Long id, Admin admin) {
        Admin adminFromDB = adminRepository.findById(id)
                .orElseThrow(() -> new AdminException(AdminError.ADMIN_IS_NOT_ACTIVE));
        if(admin.getFirstName() != null){
            adminFromDB.setFirstName(admin.getFirstName());
        }
        if(admin.getLastName() != null){
            adminFromDB.setLastName(admin.getLastName());
        }
        if(admin.getStatus() != null){
            adminFromDB.setStatus(admin.getStatus());
        }
        return adminRepository.save(adminFromDB);
    }

    @Override
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminException(AdminError.ADMIN_NOT_FOUND));
        admin.setStatus(Status.INACTIVE);
        adminRepository.save(admin);
    }
}
