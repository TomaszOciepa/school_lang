package com.tom.studentservice.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
}