package com.tom.registration_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credential {
    private String type;
    private String value;
    private boolean temporary;

    public Credential(String type, String value, boolean temporary) {
        this.type = type;
        this.value = value;
        this.temporary = temporary;
    }
}
