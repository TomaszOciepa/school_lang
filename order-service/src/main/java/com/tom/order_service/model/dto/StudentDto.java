package com.tom.order_service.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private StudentStatus status;

    public StudentDto(Long id, String firstName, String lastName, String email, StudentStatus status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
    }
}