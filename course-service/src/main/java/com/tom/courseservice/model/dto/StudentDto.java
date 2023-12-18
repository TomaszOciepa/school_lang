package com.tom.courseservice.model.dto;

import com.tom.courseservice.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Status status;

    public StudentDto(String firstName, String lastName, String email, Status status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
    }
}
