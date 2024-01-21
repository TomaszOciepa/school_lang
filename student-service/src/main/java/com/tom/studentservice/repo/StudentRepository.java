package com.tom.studentservice.repo;

import com.tom.studentservice.model.Status;
import com.tom.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByStatus(Status status);

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Student> findAllByEmailIn (List<String> emails);
    List<Student> findAllByIdIn(List<Long> idNumbers);

//    List<Student> findAllByIdNotIn(List<Long> idNumbers);

    @Query("SELECT s FROM Student s WHERE s.id NOT IN :idNumbers AND s.status = :status")
    List<Student> findAllByIdNotInAndStatus(@Param("idNumbers") List<Long> idNumbers, @Param("status") Status status);
}
