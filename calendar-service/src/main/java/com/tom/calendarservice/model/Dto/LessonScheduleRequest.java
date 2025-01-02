package com.tom.calendarservice.model.Dto;

import com.tom.calendarservice.model.LessonFrequency;
import com.tom.calendarservice.model.TimeRange;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LessonScheduleRequest {

    private TimeRange timeRange;
    private Long lessonDuration;
    private Long teacherId;
    private String courseId;
    private LessonFrequency lessonFrequency;
}
