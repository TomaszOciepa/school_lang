package com.tom.courseservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseStudents {

    private Long id;
    private LocalDateTime enrollmentDate = LocalDateTime.now();
    private Status status;

    public CourseStudents(@NotNull Long id, Status status) {
        this.id = id;
        this.status = status;
    }
}
