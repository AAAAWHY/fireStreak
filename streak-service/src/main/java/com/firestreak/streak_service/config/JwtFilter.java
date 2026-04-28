package com.firestreak.streak_service.config;

import com.firestreak.streak_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token==null || !token.startsWith("Bearer ")){
            response.sendError(401, "invalid jwt");
            return;
        }
        token = token.substring(7);

        if(!jwtService.checkToken(token)){
            response.sendError(401, "invalid jwt");
            return;
        }

        String stringRole = jwtService.extractData(token, "role", String.class);
        GrantedAuthority role = new SimpleGrantedAuthority(stringRole);
        Long userId = jwtService.getUserId(token);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId,null, List.of(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
