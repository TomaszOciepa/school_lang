package com.tom.keycloak_client_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credential{
    private String type;
    private String value;
    private boolean temporary;

    public Credential(String type, String value, boolean temporary) {
        this.type = type;
        this.value = value;
        this.temporary = temporary;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", temporary=" + temporary +
                '}';
    }
}
