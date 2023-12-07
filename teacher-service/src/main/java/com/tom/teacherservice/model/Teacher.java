package com.tom.teacherservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TEACHER")
@Getter
@Setter
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private Status status;

    @OneToMany(mappedBy = "teacher")
    private List<MyCourse> myCourse;

    public Teacher() {
    }
}
