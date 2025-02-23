package com.example.gatewayservice.Filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    @Data
    public static class Config{ // 내부 클래스
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    public CustomFilter(){ // 부모 클래스 AbstractGatewayFilterFactory<T>의 생성자 호출
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
       return (ServerWebExchange exchange, GatewayFilterChain chain) -> {
           ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
           ServerHttpResponse response = exchange.getResponse();
           log.info("Global filter baseMessage:{}", config.getBaseMessage());
           if(config.isPreLogger()){
               log.info("Global filter start: request ip -> {}", request.getRemoteAddress());
           }
           return chain.filter(exchange).then(Mono.fromRunnable(()->
                   log.info("Global filter end: response code -> {}", response.getStatusCode())
           ));
        };
    }
}
