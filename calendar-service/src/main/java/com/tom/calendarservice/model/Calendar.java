package com.tom.calendarservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Calendar {

    @Id
    private String id;
    private String eventName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long teacherId;
    private String courseId;
    private Status status;
    private String description;
    private List<AttendanceList> attendanceList = new ArrayList<>();

    public Calendar() {
    }
}
