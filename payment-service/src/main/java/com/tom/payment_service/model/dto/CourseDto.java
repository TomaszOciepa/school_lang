package com.tom.payment_service.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String id;
    private String name;
    private Long participantsLimit;
    private Long participantsNumber;
    private Long lessonsLimit;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String price;
    private Status status;
    private Long coursePrice;
    private Long pricePerLesson;
    private Long teacherSharePercentage;

}
