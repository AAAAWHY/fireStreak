package com.firestreak.user_service.service;

import com.firestreak.user_service.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

import static java.lang.System.*;

@Service
public class JwtService {
    @Value("${JWT_SECRET}")
    String secret;
    SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Base64.decode(secret));
    }

    public String generateJWT(Long userId, Role role){
        return Jwts.builder()
                .subject(""+userId)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(secretKey)
                .compact();
    }
}