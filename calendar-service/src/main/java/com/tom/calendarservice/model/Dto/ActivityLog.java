package com.tom.calendarservice.model.Dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActivityLog {
    private String eventName;
    private LocalDateTime timestamp;
    private User actor;
}
