package com.tom.courseservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseTeachers {

    private Long id;
    private LocalDateTime enrollmentData = LocalDateTime.now();
    private Status status = Status.ACTIVE;

    public CourseTeachers(@NotNull Long id) {
        this.id = id;
    }

}
