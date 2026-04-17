package com.firestreak.user_service.config;

import com.firestreak.user_service.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        if(jwtToken==null || !jwtToken.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwtToken = jwtToken.substring(7);
        if(!jwtService.checkToken(jwtToken)) {
            response.sendError(401, "invalid jwt");
            return;
        }
        Long userId = jwtService.extractId(jwtToken);
        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + jwtService.extractData(jwtToken, "role", String.class));
        Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, List.of(role));
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}