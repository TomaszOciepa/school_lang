package com.tom.calendarservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LessonMembers {

    private Long studentId;
    private boolean isPresent = false;

}
