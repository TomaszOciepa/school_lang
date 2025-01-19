package com.tom.calendarservice.model.Dto;

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