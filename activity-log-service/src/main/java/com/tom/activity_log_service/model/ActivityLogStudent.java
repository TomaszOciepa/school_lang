package com.tom.activity_log_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ActivityLogStudent {

    @Id
    private String id;
    private Long studentId;
    private ActivityLog activityLog;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public ActivityLog getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(ActivityLog activityLog) {
        this.activityLog = activityLog;
    }

    @Override
    public String toString() {
        return "ActivityLogStudent{" +
                "id='" + id + '\'' +
                ", studentId=" + studentId +
                ", activityLog=" + activityLog +
                '}';
    }
}
