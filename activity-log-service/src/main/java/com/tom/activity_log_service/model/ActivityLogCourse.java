package com.tom.activity_log_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ActivityLogCourse {

    @Id
    private String id;
    private String courseId;
    private ActivityLog activityLog;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public ActivityLog getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(ActivityLog activityLog) {
        this.activityLog = activityLog;
    }

    @Override
    public String toString() {
        return "ActivityLogCourse{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", activityLog=" + activityLog +
                '}';
    }
}
