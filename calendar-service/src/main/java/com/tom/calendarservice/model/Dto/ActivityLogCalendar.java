package com.tom.calendarservice.model.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActivityLogCalendar {

    private String id;
    private String calendarId;
    private String courseId;
    private ActivityLog activityLog;

}
