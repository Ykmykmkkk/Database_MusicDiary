package com.example.musicdiary.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final Environment env;

    public String getUserId(String token) {
        String secretKey = env.getProperty("token.secret");
        byte[] keyBytes = secretKey.getBytes();

        // JwtParser를 사용해 토큰 파싱
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(keyBytes)  // 서명 키 설정
                .build();

        Claims claims = parser // Claims 객체는 토큰 데이터를 가지고 있다.
                .parseClaimsJws(token)  // JWS 토큰 파싱
                .getBody();

        return claims.get("userId", String.class);
    }
}
