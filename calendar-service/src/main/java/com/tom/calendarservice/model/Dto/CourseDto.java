package com.tom.calendarservice.model.Dto;

import com.tom.calendarservice.model.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseDto {

    private String id;
    private String name;
    private Status status;
    private Long participantsLimit;
    private Long participantsNumber;
    private Long lessonsLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<CourseStudentsDto> courseStudents = new ArrayList<>();
    private List<CourseTeachersDto> courseTeachers = new ArrayList<>();
}
