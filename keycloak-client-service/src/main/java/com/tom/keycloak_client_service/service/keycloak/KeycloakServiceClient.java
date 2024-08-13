package com.tom.keycloak_client_service.service.keycloak;

import com.tom.keycloak_client_service.model.Role;
import com.tom.keycloak_client_service.model.TokenResponse;
import com.tom.keycloak_client_service.model.UserKeycloakDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping(value = "/admin/realms/mango/roles/{role}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Role getRole(@RequestHeader("Authorization") String authorization,
                 @PathVariable("role") String role);

    @GetMapping(value = "/admin/realms/mango/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UserKeycloakDto> getUser(@RequestHeader("Authorization") String authorization,
                                  @RequestParam("email") String email);

    @PostMapping(value = "/admin/realms/mango/users/{userId}/role-mappings/realm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void assignRole(@RequestHeader("Authorization") String authorization,
                    @PathVariable("userId") String userId,
                    @RequestBody List<Role> role);

    @DeleteMapping(value = "/admin/realms/mango/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteAccount(@RequestHeader("Authorization") String authorization,
                       @PathVariable("id") String id
                   );
}
