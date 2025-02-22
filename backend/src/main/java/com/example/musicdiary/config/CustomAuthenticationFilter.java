package com.example.musicdiary.config;

import com.example.musicdiary.presentation.dto.request.LoginRequestDto;
import com.example.musicdiary.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    Environment env;
    UserService userService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.env = env;
        this.userService = userService;
    }

    @Override // UsenamePasswordAuthenticationFilter가 가장 먼저 실행 하는 메소드. 인증 과정 실행
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try{
            LoginRequestDto loginRequestDto =
                    new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
                            loginRequestDto.getPassword(), new ArrayList<>())
            );
        }
        catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override // 인증 성공인 경우
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String userName = ((User)authResult.getPrincipal()).getUsername();
        // Authentication 인터페이스의 getPrincipal() 메서드가 반환하는 객체를 User 클래스로 캐스팅하
    }
}
