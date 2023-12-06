package com.tom.studentservice.model;

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
    private LocalDateTime enrollmentDate;
    private String courseName;

    public MyCourse(String courseName) {
        this.enrollmentDate = LocalDateTime.now();
        this.courseName = courseName;
    }

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public MyCourse() {
    }
}
