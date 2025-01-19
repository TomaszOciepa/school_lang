package com.tom.studentservice.model.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActivityLogStudent {

    private String id;
    private Long studentId;
    private ActivityLog activityLog;
}
