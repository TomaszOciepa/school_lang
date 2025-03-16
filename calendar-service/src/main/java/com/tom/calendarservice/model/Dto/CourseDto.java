package com.tom.calendarservice.model.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tom.calendarservice.model.Language;
import com.tom.calendarservice.model.LessonFrequency;
import com.tom.calendarservice.model.Status;
import com.tom.calendarservice.model.TimeRange;
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
    private Long coursePrice;
    private Long pricePerLesson;
    private Long teacherSharePercentage;
    private Language language;
    private Long participantsLimit;
    private Long participantsNumber;
    private Long lessonsLimit;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private List<CourseStudentsDto> courseStudents = new ArrayList<>();
    private List<CourseTeachersDto> courseTeachers = new ArrayList<>();
    private TimeRange timeRange;
    private Long lessonDuration;
    private Long teacherId;
    private LessonFrequency lessonFrequency;
}
