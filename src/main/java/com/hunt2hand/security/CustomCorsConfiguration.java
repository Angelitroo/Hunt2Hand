package com.hunt2hand.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
public class CustomCorsConfiguration  {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        List<String> allowedOrigins = List.of("https://hunt2handfront.onrender.com/", "https://localhost", "capacitor://localhost", "http://localhost:4200");
        for (String origin : allowedOrigins) {
            corsConfiguration.addAllowedOrigin(origin);
        }

        List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        for (String method : allowedMethods) {
            corsConfiguration.addAllowedMethod(method);
        }

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}