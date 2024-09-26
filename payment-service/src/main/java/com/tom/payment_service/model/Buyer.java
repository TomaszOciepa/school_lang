package com.tom.payment_service.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Buyer {
    private String email;
    private String firstName;
    private String lastName;
}
