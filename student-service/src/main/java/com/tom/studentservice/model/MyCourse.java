package com.tom.studentservice.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyCourse {

    private LocalDateTime enrollmentDate;
    private String courseName;

    public MyCourse(String courseName) {
        this.enrollmentDate = LocalDateTime.now();
        this.courseName = courseName;
    }
}
