package com.tom.adminservice.repo;


import com.tom.adminservice.model.Admin;
import com.tom.adminservice.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    List<Admin> findAllByStatus(Status status);

    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);
}
