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
public class ActivityLogTeacher {

    @Id
    private String id;
    private String teacherId;
    private ActivityLog activityLog;
}
