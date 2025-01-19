package com.tom.activity_log_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ActivityLogCalendar {

    @Id
    private String id;
    private String calendarId;
    private String courseId;
    private ActivityLog activityLog;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
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
        return "ActivityLogCalendar{" +
                "id='" + id + '\'' +
                ", calendarId='" + calendarId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", activityLog=" + activityLog +
                '}';
    }
}
