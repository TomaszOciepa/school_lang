package com.tom.teacherservice;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String TEACHER_SERVICE = "teacher-service";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");

        Map<String, Object> clientRoles = (Map<String, Object>) realmAccess.get(TEACHER_SERVICE);

        if(clientRoles == null || clientRoles.isEmpty()){
            return new ArrayList<>();
        }

        Collection<GrantedAuthority> returnValue = ((List<String>) clientRoles.get("roles"))
                .stream()
                .map(role -> "ROLE_"+role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());



        return returnValue;
    }
}
