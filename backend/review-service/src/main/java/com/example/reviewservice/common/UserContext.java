package com.example.reviewservice.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Component
public class UserContext {
    public UUID getCurrentUserId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes==null){
            throw new IllegalArgumentException("RequestContext is not available");
        }
        // 현재 HTTP 요청 객체 얻기
        HttpServletRequest request = attributes.getRequest();

        // 요청 헤더에서 X-User-Id 값 가져오기
        String userIdHeader = request.getHeader("X-User-Id");

        if (userIdHeader == null || userIdHeader.isEmpty()) {
            throw new IllegalArgumentException("X-User-Id header is missing");
        }

        return UUID.fromString(userIdHeader);
    }
}
