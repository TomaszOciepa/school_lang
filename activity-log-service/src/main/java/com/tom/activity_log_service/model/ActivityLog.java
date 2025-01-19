package com.tom.activity_log_service.model;



import java.time.LocalDateTime;

public class ActivityLog {

    private String eventName;
    private LocalDateTime timestamp;
    private User actor;


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
                "eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                ", actor=" + actor +
                '}';
    }
}
