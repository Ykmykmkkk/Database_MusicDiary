package com.example.userservice.security;

import com.example.userservice.presentation.dto.requestDto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    Environment env;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Environment env) {
        super(authenticationManager);
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequestDto loginRequestDto =
                    new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            // jackson의 objectMapper를 이용해 LoginRequestDto 가져오기

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
                            loginRequestDto.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid request body");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return null; // 해당 인증 검증 로직이 끝났다
        }
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String errorMessage = "{\"error\": \"로그인 실패: " + failed.getMessage() + "\"}";
        // loadUserByUsername 서비스에서 throw한 UsernameNotFoundException(해당 회원이 존재하지 않습니다)가
        // failed 객체로 넘어오길 기대했지만, AuthenticationProvider에서 보안 상의 이유로 아이디가 틀렸는지 비밀번호가 틀렸는지
        // 구체적으로 설명해주지 않기 위해 setHideUserNotFoundExceptions(true); 설정이 default로 되어 있다. 보안 상 이유!!
        // *UsernameNotFoundException은 AuthenticationException의 하위 클래스이다.
        response.getWriter().write(errorMessage);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        if (!(authResult.getPrincipal() instanceof CustomUserDetails customUserDetails)) {
            throw new IllegalStateException("Principal must be an instance of CustomUserDetails");
        }

        generateToken(response, customUserDetails.getId());

        chain.doFilter(request,response); // 후속 처리 진행
    }
    protected void generateToken(HttpServletResponse response, UUID userId) throws IOException {
        byte[] keyBytes = env.getProperty("TOKEN_SECRET").getBytes();
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("Secret key must be at least 512 bits (64 bytes) for HS512");
        }
        var key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(System.currentTimeMillis()
                        + Long.parseLong(env.getProperty("TOKEN_EXPRIATION_TIME"))))
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuer(env.getProperty("TOKEN_ISSUER"))
                .compact(); // compact를 통해 Jwt 토큰을 생성

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
}
