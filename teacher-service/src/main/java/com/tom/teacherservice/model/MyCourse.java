package com.tom.teacherservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MY_COURSE")
@Getter
@Setter
@AllArgsConstructor
public class MyCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime courseEnrollment;
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public MyCourse() {
    }
}
