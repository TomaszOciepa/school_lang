package com.tom.calendarservice.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class CourseDto {

    private String id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<CourseStudentsDto> courseStudents = new ArrayList<>();
    private List<CourseTeachersDto> courseTeachers = new ArrayList<>();
}
