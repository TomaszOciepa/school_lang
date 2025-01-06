package com.tom.calendarservice.model.Dto;

import com.tom.calendarservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseStudentsDto {

    private Long id;
    private LocalDateTime enrollmentDate;
    private Status status;
}
