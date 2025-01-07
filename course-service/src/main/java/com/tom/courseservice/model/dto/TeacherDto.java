package com.tom.courseservice.model.dto;

import com.tom.courseservice.model.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Status status;

    public TeacherDto(Long id, String firstName, String lastName, String email, Status status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
    }
}
