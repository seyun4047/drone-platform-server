package com.mutzin.droneplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ★ POST 막는 주범
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/telemetry").permitAll()
                        .requestMatchers("/auth/*").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
