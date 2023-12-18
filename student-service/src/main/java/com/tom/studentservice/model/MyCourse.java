package com.tom.studentservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private Student student;
    public MyCourse() {
    }

    public MyCourse(String courseName, Student student) {
        this.enrollmentDate = LocalDateTime.now();
        this.courseName = courseName;
        this.student = student;
    }

    @Override
    public String toString() {
        return "MyCourse{" +
                "id=" + id +
                ", enrollmentDate=" + enrollmentDate +
                ", courseName='" + courseName + '\'' +
                ", student=" + student +
                '}';
    }
}
