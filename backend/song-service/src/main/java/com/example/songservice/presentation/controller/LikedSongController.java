package com.example.songservice.presentation.controller;

import com.example.songservice.application.LikedSongService;
import com.example.songservice.common.SongDto;
import com.example.songservice.presentation.dto.requestDto.SetSongLikeRequestDto;
import com.example.songservice.presentation.dto.responseDto.SongResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/")
public class LikedSongController {
    private final LikedSongService likedSongService;
    @PostMapping("/like/{songId}")
    public ResponseEntity<?> setSongLike(@RequestHeader("X-User-Id") UUID userId, @PathVariable Long songId) {
        try {
            likedSongService.likeSong(userId,songId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/like/{songId}")
    public ResponseEntity<?> setSongUnlike(@RequestHeader("X-User-Id") UUID userId, @PathVariable Long songId) {
        try {
            likedSongService.unlikeSong(userId,songId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/like/all")
    public ResponseEntity<?> getSongLike(@RequestHeader("X-User-Id") UUID userId) {
        try {
            log.info("여긴 왔다");
            List<SongDto> likedSongList = likedSongService.getLikedSongListByUserId(userId);
            List<SongResponseDto> responseDtoList = likedSongList.stream().map(songDto ->
                    SongResponseDto.builder()
                            .album(songDto.getAlbum())
                            .durationTime(songDto.getDurationTime())
                            .releaseDate(songDto.getReleaseDate())
                            .artist(songDto.getArtist())
                            .title(songDto.getTitle())
                            .isLike(songDto.getIsLike())
                            .build()
            ).toList();
            log.info("여긴 왔다2");
            return ResponseEntity.ok(responseDtoList);
        }
        catch (Exception e) {
            log.info("여긴 왔다3");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/like/{songId}")
    public ResponseEntity<Boolean> isLikedSong(@RequestHeader("X-User-Id") UUID userId, @PathVariable Long songId) {
        try {
            boolean isLiked = likedSongService.isLikedSong(userId, songId);
            return ResponseEntity.ok(isLiked);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}
