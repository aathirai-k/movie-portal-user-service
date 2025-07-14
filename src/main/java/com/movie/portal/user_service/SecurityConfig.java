package com.movie.portal.user_service.auth;

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
                .csrf().disable()  // disable CSRF if you want, or configure it properly
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/register").permitAll()  // allow register without auth
                        .anyRequest().authenticated()  // secure everything else
                )
                .httpBasic();  // or your authentication method
        return http.build();
    }
}
