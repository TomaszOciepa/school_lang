package com.tom.salary_service.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Pobierz obiekt Authentication z kontekstu Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Sprawdź, czy obiekt Authentication jest JwtAuthenticationToken
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

            // Pobierz token JWT z obiektu JwtAuthenticationToken
            String token = jwtAuthenticationToken.getToken().getTokenValue();

            // Dodaj token do nagłówka "Authorization" w formie "Bearer <token>"
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
