package com.tom.courseservice.model.dto;

import com.tom.courseservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CourseStudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDateTime enrollmentDate;
    private Status status;
}
