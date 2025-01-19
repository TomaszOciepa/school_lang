package com.tom.teacherservice.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActivityLogTeacher {

    private String id;
    private Long teacherId;
    private ActivityLog activityLog;
}
