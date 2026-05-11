package com.firestreak.gateway.config;

import com.firestreak.gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    private final JwtService jwtService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(token==null || !token.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
            byte[] bytes = "invalid jwt".getBytes(StandardCharsets.UTF_8);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        }
        token = token.substring(7);

        if(!jwtService.checkToken(token)){
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
            byte[] bytes = "invalid jwt".getBytes(StandardCharsets.UTF_8);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        }
        String userId = ""+jwtService.getUserId(token);
        String roleStr = jwtService.extractData(token, "role", String.class);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-user-id", userId)
                .header("X-role", roleStr)
                .build();

        exchange = exchange.mutate().request(mutatedRequest).build();
        return chain.filter(exchange);
    }
}