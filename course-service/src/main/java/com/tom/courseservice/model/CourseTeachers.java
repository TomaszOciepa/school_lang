package com.tom.courseservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CourseTeachers {

    private String teacherId;
    private LocalDateTime enrollmentData;

    public CourseTeachers(@NotNull String teacherId) {
        this.teacherId = teacherId;
        this.enrollmentData = LocalDateTime.now();
    }
}
