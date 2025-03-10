package com.example.reviewservice.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "song-service")
public interface SongClient {
    // songId를 받아서 좋아요 여부 반환
    @GetMapping("/like/{songId}/")
    Boolean isSongLiked(@PathVariable String songId);
}
