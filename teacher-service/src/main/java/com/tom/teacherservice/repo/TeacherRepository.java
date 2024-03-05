package com.tom.teacherservice.repo;

import com.tom.teacherservice.model.Status;
import com.tom.teacherservice.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    //sprawdzone
    List<Teacher> findAllByIdIn(List<Long> idNumbers);
    @Query("SELECT t FROM Teacher t WHERE t.id NOT IN :idNumbers AND t.status = :status")
    List<Teacher> findAllByIdNotInAndStatus(@Param("idNumbers") List<Long> idNumbers, @Param("status") Status status);
    //    nie sprawdzone
    List<Teacher> findAllByStatus(Status status);

    Optional<Teacher> findByEmail(String email);

    boolean existsByEmail(String email);



}
