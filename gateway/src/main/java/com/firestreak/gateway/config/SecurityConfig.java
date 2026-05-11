package com.firestreak.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity.csrf(Csrf -> Csrf.disable())
                .authorizeExchange((auth) -> auth
                        .pathMatchers("/api/users/registration", "/api/users/login").permitAll()
                        .anyExchange().authenticated())
                .build();
    }
}
