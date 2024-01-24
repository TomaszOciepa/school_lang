package com.tom.calendarservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceList {

    private Long studentId;
    private boolean isPresent = false;

}
