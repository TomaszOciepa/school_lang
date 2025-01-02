package com.tom.courseservice.model;

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
