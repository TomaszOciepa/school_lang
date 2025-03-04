package com.tom.calendarservice.model.Dto;

import com.tom.calendarservice.model.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Status status;
    private String password;

    public StudentDto(Long id, String firstName, String lastName, String email, Status status, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.password = password;
    }
}
