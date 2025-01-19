package com.tom.courseservice.model.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActivityLogCourse {
    private String id;
    private String courseId;
    private ActivityLog activityLog;
}
