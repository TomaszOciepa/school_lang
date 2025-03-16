package com.tom.keycloak_client_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {

    private String id;
    private String name;

    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
