package com.tom.activity_log_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActivityLogCalendar {

    @Id
    private String id;
    private String calendarId;
    private ActivityLog activityLog;
}
