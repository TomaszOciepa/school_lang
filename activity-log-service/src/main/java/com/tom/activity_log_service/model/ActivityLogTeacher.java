package com.tom.activity_log_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ActivityLogTeacher {

    @Id
    private String id;
    private Long teacherId;
    private ActivityLog activityLog;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public ActivityLog getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(ActivityLog activityLog) {
        this.activityLog = activityLog;
    }

    @Override
    public String toString() {
        return "ActivityLogTeacher{" +
                "id='" + id + '\'' +
                ", teacherId=" + teacherId +
                ", activityLog=" + activityLog +
                '}';
    }
}
