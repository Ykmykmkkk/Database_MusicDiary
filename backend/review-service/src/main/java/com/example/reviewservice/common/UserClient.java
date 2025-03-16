package com.example.reviewservice.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/{userId}/")
    ResponseEntity<String> getUsername(@PathVariable UUID userId);
}
