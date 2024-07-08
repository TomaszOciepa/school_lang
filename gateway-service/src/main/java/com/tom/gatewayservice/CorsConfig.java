package com.tom.gatewayservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200"); // Allow your frontend's origin
        config.addAllowedHeader("*");
        config.addAllowedMethod(RequestMethod.GET.name());
        config.addAllowedMethod(RequestMethod.POST.name());
        config.addAllowedMethod(RequestMethod.DELETE.name());
        config.addAllowedMethod(RequestMethod.PATCH.name());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}