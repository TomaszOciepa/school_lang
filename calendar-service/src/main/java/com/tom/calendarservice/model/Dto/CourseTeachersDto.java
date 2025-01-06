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
public class CourseTeachersDto {

    private Long id;
    private LocalDateTime enrollmentData;
    private Status status;
}
