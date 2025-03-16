package com.tom.keycloak_client_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse {
    private String access_token;
    private String token_type;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private int not_before_policy;
    private String session_state;
    private String scope;
}
