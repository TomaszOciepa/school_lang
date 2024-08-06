package com.tom.registration_service.service;


import com.tom.registration_service.model.TokenResponse;
import com.tom.registration_service.model.UserKeycloakDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;


@FeignClient(name = "keycloak", url = "http://localhost:8030")
public interface KeycloakServiceClient {

    @PostMapping(value = "/realms/master/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getRootToken(@RequestBody Map<String, ?> formParams);

    @PostMapping(value = "/realms/mango/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenResponse getAdminToken(@RequestBody Map<String, ?> formParams);

    @PostMapping(value = "/admin/realms/mango/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createUser(@RequestHeader("Authorization") String authorization,
                    @RequestBody UserKeycloakDto userKeycloakDto);
}
