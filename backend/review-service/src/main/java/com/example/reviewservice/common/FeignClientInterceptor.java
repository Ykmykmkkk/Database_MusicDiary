package com.example.reviewservice.common;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    // Feign Client는 내부적으로 HTTP 요청을 보내기 때문에,
    // Spring MVC 기반의 애플리케이션에서는 새로운 별개의 ServletRequest를 생성해서 원격 API를 호출
    // 이 때문에 요청과 관련된 @RequestHeader, @RequestParam, @RequestBody 같은 정보가 기본적으로 전달되지 않아서,
    // 필요하면 RequestInterceptor를 설정해줘야 한다.
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
