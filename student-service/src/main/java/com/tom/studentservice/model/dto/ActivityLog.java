package com.tom.studentservice.model.dto;

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

