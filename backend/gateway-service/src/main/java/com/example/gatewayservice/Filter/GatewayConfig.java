package com.example.gatewayservice.Filter;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder,
            JwtFilter jwtFilter,
            CustomFilter customFilter) {
        return builder.routes()
                // user-service: /user/login
                .route("user-service-login", r -> r.path("/user/login")
                        .and().method("POST")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                .rewritePath("/user/(?<segment>.*)", "/${segment}")
                                .filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("lb://USER-SERVICE"))

                // user-service: /user/duplicate
                .route("user-service-duplicate", r -> r.path("/user/duplicate")
                        .and().method("POST")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                .rewritePath("/user/(?<segment>.*)", "/${segment}")
                                .filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("lb://USER-SERVICE"))

                // user-service: /user/register
                .route("user-service-register", r -> r.path("/user/register")
                        .and().method("POST")
                        .filters(f -> f
                                .removeRequestHeader("Cookie")
                                .rewritePath("/user/(?<segment>.*)", "/${segment}")
                                .filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("lb://USER-SERVICE"))

                // song-service: /song/**
                .route("song-service", r -> r.path("/song/**")
                        .filters(f -> f
                                .rewritePath("/song/(?<segment>.*)", "/${segment}")
                                .filter(jwtFilter.apply(new JwtFilter.Config()))
                                .filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("lb://SONG-SERVICE"))

                // review-service: /review/**
                .route("review-service", r -> r.path("/review/**")
                        .filters(f -> f
                                .rewritePath("/review/(?<segment>.*)", "/${segment}")
                                .filter(jwtFilter.apply(new JwtFilter.Config()))
                                .filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("lb://REVIEW-SERVICE"))

                .build();
    }
}
