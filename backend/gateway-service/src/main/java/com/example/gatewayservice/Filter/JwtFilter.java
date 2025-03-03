package com.example.gatewayservice.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Slf4j
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {
    private final Environment env;

    public static class Config {
        // 설정이 필요하면 여기에 추가
    }

    public JwtFilter(Environment env) { // 부모 클래스 AbstractGatewayFilterFactory<T>의 생성자 호출
        super(JwtFilter.Config.class);
        this.env=env;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders()
                    .get(HttpHeaders.AUTHORIZATION)
                    .get(0);
            String jwt = authorizationHeader.replace("Bearer ", "").trim();

            String userId = extractUserId(jwt);

            if (userId==null) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            // 토큰이 유효한지 검증이 완료되면 header에 "X-User-Id"를 추가해서 각 마이크로서비스에 수정된 request를 보낸다.
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .build(); // 기존의 request에 header를 추가한 modifiedRequest 생성

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build(); // modifiedRequest를 반영한 ServerWebExchange 생성해서 반영

            return chain.filter(modifiedExchange);
        };
    }
    // Mono는 단일 값, Flux는 다중 값 -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error("Request failed: {} - Path: {}", err, exchange.getRequest().getPath());
        return response.setComplete();
    }

    //userId 추출 및 토큰 검증 로직
    private String extractUserId(String jwt) {
        try {
            byte[] base64Key = env.getProperty("TOKEN_SECRET").getBytes();
            String expectedIssuer = env.getProperty("TOKEN_ISSUER");

            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(base64Key)
                    .build(); // JwtParser를 빌드 해 토큰 파싱. 토큰을 만들 때 사용한 byte 타입의 key를 넣어주어야 한다.

            Claims claims = parser.parseClaimsJws(jwt).getBody();
            // Claims 객체는 토큰 데이터를 가진다.

            String userId = claims.getSubject();
            if (userId == null || userId.isEmpty()) {
                log.error("Invalid userId in JWT");
                return null;
            } // userId 추출

            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                log.error("JWT is expired");
                return null;
            } // 만료 시간 확인

            String issuer = claims.getIssuer();
            if (!expectedIssuer.equals(issuer)) {
                log.error("Invalid issuer: expected {}, got {}", expectedIssuer, issuer);
                return null;
            } // 발급자 검증

            return userId;

        } catch (SignatureException e) { // 토큰의 서명(Signature)이 제공된 secretKey로 만들어진 것인지 확인
            log.error("Invalid JWT signature: {}", e.getMessage());
            return null;
        } catch (MalformedJwtException e) { // 토큰이 올바른 JWT 형식인지(헤더, 페이로드, 서명으로 구성된 3단계 구조)
            log.error("Malformed JWT: {}", e.getMessage());
            return null;
        } catch (Exception e) { // 이외 에러 처리
            log.error("JWT validation error: {}", e.getMessage());
            return null;
        }
    }
}