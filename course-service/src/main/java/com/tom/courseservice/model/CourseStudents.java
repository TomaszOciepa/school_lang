package com.tom.courseservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseStudents {

    private Long studentId;
    private LocalDateTime enrollmentDate;
    private Status status;

    public CourseStudents(@NotNull Long studentId, Status status) {
        this.studentId = studentId;
        this.enrollmentDate = LocalDateTime.now();
        this.status = status;
    }
}
