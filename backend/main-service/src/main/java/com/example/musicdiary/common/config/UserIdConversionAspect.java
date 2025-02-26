package com.example.musicdiary.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class UserIdConversionAspect {
    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void convertUserIdToLong() {
        // 현재 요청의 HttpServletRequest 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return; // 요청 컨텍스트가 없으면 종료
        }

        HttpServletRequest request = attributes.getRequest();
        String userIdStr = request.getHeader("X-User-Id"); // 헤더에서 String userId 가져오기

        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                Long userId = Long.parseLong(userIdStr); // String -> Long 변환
                request.setAttribute("userId", userId); // 변환된 값 저장
            } catch (NumberFormatException e) {
                // 변환 실패 시 예외 처리 (필요에 따라 로깅 또는 기본값 설정)
                request.setAttribute("userId", null);
            }
        }
    }
}
