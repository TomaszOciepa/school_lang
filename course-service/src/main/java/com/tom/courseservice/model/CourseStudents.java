package com.tom.courseservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseStudents {

    private String studentId;
    private LocalDateTime enrollmentDate;

    public CourseStudents(@NotNull String studentId) {
        this.studentId = studentId;
        this.enrollmentDate = LocalDateTime.now();
    }
}