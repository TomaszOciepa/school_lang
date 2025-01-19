package com.tom.activity_log_service.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActivityLogStudent {

    private String id;
    private Long studentId;
    private ActivityLog activityLog;

}
