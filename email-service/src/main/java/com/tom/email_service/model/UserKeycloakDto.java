package com.tom.email_service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserKeycloakDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private boolean enabled;
    private List<Credential> credentials;

    public UserKeycloakDto(String firstName, String lastName, String email, boolean emailVerified, boolean enabled,
                           List<Credential> credentials
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.enabled = enabled;
        this.credentials = credentials;
    }

    public UserKeycloakDto() {
    }

    @Override
    public String toString() {
        return "UserKeycloakDto{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", enabled=" + enabled +
                ", credentials=" + credentials +
                '}';
    }
}
