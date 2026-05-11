package com.firestreak.gateway.config;

import com.firestreak.gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity, JwtFilter jwtFilter){
        return httpSecurity.csrf(Csrf -> Csrf.disable())
                .authorizeExchange((auth) -> auth
                        .pathMatchers("/api/users/registration", "/api/users/login").permitAll()
                        .anyExchange().authenticated())
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    JwtFilter jwtFilter(JwtService jwtService){return new JwtFilter(jwtService);}
}
