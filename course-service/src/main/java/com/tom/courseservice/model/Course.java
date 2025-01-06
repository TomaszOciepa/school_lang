package com.tom.courseservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course {

    @Id
    private String id;
    private String name;
    private Status status;
    private String price;
    private Language language;
    private Long participantsLimit;
    private Long participantsNumber;
    private Long lessonsLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<CourseStudents> courseStudents = new ArrayList<>();
    private List<CourseTeachers> courseTeachers = new ArrayList<>();
    private TimeRange timeRange;
    private Long lessonDuration;
    private Long teacherId;
    private LessonFrequency lessonFrequency;

    public void incrementParticipantsNumber(){
        participantsNumber++;
    }

    public void decrementParticipantsNumber(){
        participantsNumber--;
    }

}
