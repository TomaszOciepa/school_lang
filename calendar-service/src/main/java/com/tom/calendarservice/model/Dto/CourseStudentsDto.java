package com.tom.calendarservice.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CourseStudentsDto {

    private Long studentId;
    private LocalDateTime enrollmentDate;
}
