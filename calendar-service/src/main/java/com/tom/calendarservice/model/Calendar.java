package com.tom.calendarservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
public class Calendar {

    @Id
    private String id;
    private String eventName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long teacherId;
    private String courseId;
    private String description;
    private List<Long> studentIdList;

    public Calendar(String eventName, LocalDateTime startDate, LocalDateTime endDate, Long teacherId, String courseId, String description, List<Long> studentIdList) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.description = description;
        this.studentIdList = studentIdList;
    }
    public Calendar() {
    }
}
