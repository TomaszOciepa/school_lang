package com.tom.calendarservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private String studentId;
    private String teacherId;
    private String courseId;
    private String description;
}
