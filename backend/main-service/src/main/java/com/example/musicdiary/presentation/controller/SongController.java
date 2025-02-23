package com.example.musicdiary.presentation.controller;


import com.example.musicdiary.presentation.dto.request.CreateSongRequestDto;
import com.example.musicdiary.presentation.dto.request.SetSongLikeRequestDto;
import com.example.musicdiary.presentation.dto.response.SongResponseDto;
import com.example.musicdiary.service.LikedSongService;
import com.example.musicdiary.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {
    private final SongService songService;
    private final LikedSongService likedSongService;

    @PostMapping("/create")
    public ResponseEntity<?> createSong(@RequestBody CreateSongRequestDto createSongRequestDto) {
        try {
            songService.createSong(createSongRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("SongEntity created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{title}/{artist}")
    public ResponseEntity<?> getSongByTitleAndArtist( @PathVariable String title,
                                                      @PathVariable String artist) {
        try {
            SongResponseDto song = songService.getSongByTitleAndArtist(title, artist);
            return ResponseEntity.ok(song);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/like")
    public ResponseEntity<?> setSongLike(@RequestHeader("username") String username, @RequestBody SetSongLikeRequestDto setSongLikeRequestDto) {
        try {
            likedSongService.setSongLike(username,setSongLikeRequestDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> setSongUnlike(@RequestHeader("username") String username, @RequestBody SetSongLikeRequestDto setSongLikeRequestDto) {
        try {
            likedSongService.setSongUnlike(username,setSongLikeRequestDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/like")
    public ResponseEntity<?> getSongLike(@RequestHeader("username") String username) {
        try {
            return ResponseEntity.ok(likedSongService.getLikedSongListByUsername(username));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
