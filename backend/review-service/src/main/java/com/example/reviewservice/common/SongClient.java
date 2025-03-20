package com.example.reviewservice.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "song-service")
public interface SongClient {
    // songId를 받아서 좋아요 여부 반환
    @GetMapping("/like/{songId}")
    ResponseEntity<Boolean> isLikedSong(@PathVariable("songId") Long songId);
    @GetMapping("/{songId}")
    ResponseEntity<SongResponseDto> getSongTitleAndArtist(@PathVariable("songId") Long songId);


}
