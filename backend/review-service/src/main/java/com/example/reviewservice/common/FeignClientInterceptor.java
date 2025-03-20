package com.example.reviewservice.common;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    // 이 인터페이스를 구현한 클래스는 Feign이 HTTP 요청을 생성하기 전에 RequestTemplate을 수정할 수 있다(헤더 추가, URL 변경 등).
    private final UserContext userContext;

    public FeignClientInterceptor(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public void apply(RequestTemplate template) {
        UUID userId = userContext.getCurrentUserId();
        template.header("X-User-Id", userId.toString());
    }
}
