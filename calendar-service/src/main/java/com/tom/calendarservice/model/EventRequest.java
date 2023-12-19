package com.tom.calendarservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class EventRequest {

    private String courseId;
    private Long teacherId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
}
