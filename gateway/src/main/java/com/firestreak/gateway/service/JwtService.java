package com.firestreak.gateway.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class JwtService {
    @Value("${JWT_SECRET}")
    String jwtSecret;
    SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }

    public boolean checkToken(String token){
        try {
            Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public Long getUserId(String token){
        return Long.parseLong(Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public <T> T extractData(String token, String claimName, Class<T> classType){
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get(claimName, classType);
    }
}
