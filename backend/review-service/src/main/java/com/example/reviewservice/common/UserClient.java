package com.example.reviewservice.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/{userId}") // writerUsername 가져오기
    ResponseEntity<String> getUsername(@PathVariable("userId") UUID userId);
}
