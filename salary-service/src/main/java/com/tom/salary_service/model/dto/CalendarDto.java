package com.tom.salary_service.model.dto;

import com.tom.salary_service.model.Status;


import java.time.LocalDateTime;



public class CalendarDto {

    private String id;
    private String eventName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long teacherId;
    private String courseId;
    private Status status;
    private String description;
    private Language language;
    private Long price;

    public CalendarDto() {
    }

    public CalendarDto(String id, String eventName, LocalDateTime startDate, LocalDateTime endDate, Long teacherId, String courseId, Status status, String description, Language language, Long price) {
        this.id = id;
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.status = status;
        this.description = description;
        this.language = language;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Language getLanguage() {
        return language;
    }

    public Long getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


}
