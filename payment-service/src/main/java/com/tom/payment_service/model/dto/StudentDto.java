package com.tom.payment_service.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;


}
