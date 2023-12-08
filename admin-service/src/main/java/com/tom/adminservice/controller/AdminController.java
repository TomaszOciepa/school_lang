package com.tom.adminservice.controller;

import com.tom.adminservice.model.Admin;
import com.tom.adminservice.model.Status;
import com.tom.adminservice.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    List<Admin> getAllAdmins(@RequestParam(required = false) Status status){
        return adminService.getAllAdmin(status);
    }

    @GetMapping("/{id}")
    Admin getAdminById(@PathVariable Long id){
        return adminService.getAdminById(id);
    }

    @GetMapping("/email")
    Admin getAdminByEmail(@RequestParam String email){
        return adminService.getAdminByEmail(email);
    }

    @PostMapping
    Admin addAdmin(@RequestBody Admin admin){
        return adminService.addAdmin(admin);
    }

    @PutMapping("/{id}")
    Admin putAdmin(@PathVariable Long id, @RequestBody Admin admin){
        return adminService.putAdmin(id, admin);
    }

    @PatchMapping("/{id}")
    Admin patchAdmin(@PathVariable Long id, @RequestBody Admin admin){
        return adminService.patchAdmin(id, admin);
    }

    @DeleteMapping("/{id}")
    void deleteAdmin(@PathVariable Long id){
        adminService.deleteAdmin(id);
    }

}
