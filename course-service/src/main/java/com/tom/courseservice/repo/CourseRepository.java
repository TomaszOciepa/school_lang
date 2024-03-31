package com.tom.courseservice.repo;

import com.tom.courseservice.model.Course;
import com.tom.courseservice.model.Language;
import com.tom.courseservice.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {

    //sprawdzone
    boolean existsByName(String name);
    List<Course> getAllByStatus(Status status);
    Optional<Course> findByIdAndStatus(String id, Status status);

    @Query("{'courseTeachers.id': ?0}")
    List<Course> getCoursesByTeacherId(Long teacherId);

    @Query("{'courseStudents.id': ?0}")
    List<Course> getCoursesByStudentId(Long studentId);

    @Query("{'language': ?0, 'status': ?1}")
    List<Course> getCoursesByLanguage(Language language, Status status);

}
