package com.firestreak.gateway.config;

import com.firestreak.gateway.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(!token.startsWith("Bearer ")){
            response.sendError(401, "invalid jwt");
            return;
        }
        token = token.substring(7);

        if(!jwtService.checkToken(token)){
            response.sendError(401, "invalid jwt");
            return;
        }
        String userId = ""+jwtService.getUserId(token);
        String role = jwtService.extractData(token, "role", String.class);
        response.addHeader("X-user-id", userId);
        response.addHeader("X-role", role);
        filterChain.doFilter(request,response);


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null, )
        SecurityContextHolder.getContext().setAuthentication();
    }
}
