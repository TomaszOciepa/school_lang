package com.tom.salary_service.model;

import com.tom.salary_service.model.dto.CalendarDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class Salary {

    @Id
    private String id;
    private LocalDateTime date;
    private Long teacherId;
    private Long payoutAmount;
    private Status status;
    private List<CalendarDto> lessons;

    public Salary() {
    }

    public Salary(String id, LocalDateTime date, Long teacherId, Long payoutAmount, Status status, List<CalendarDto> lessons) {
        this.id = id;
        this.date = date;
        this.teacherId = teacherId;
        this.payoutAmount = payoutAmount;
        this.status = status;
        this.lessons = lessons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(Long payoutAmount) {
        this.payoutAmount = payoutAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<CalendarDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<CalendarDto> lessons) {
        this.lessons = lessons;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", teacherId=" + teacherId +
                ", payoutAmount=" + payoutAmount +
                ", status=" + status +
                ", lessons=" + lessons +
                '}';
    }
}
