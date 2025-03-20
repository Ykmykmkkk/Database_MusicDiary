package com.example.songservice.presentation.controller;

import com.example.songservice.application.SongService;
import com.example.songservice.common.SongDto;
import com.example.songservice.presentation.dto.requestDto.CreateSongRequestDto;
import com.example.songservice.presentation.dto.responseDto.SongResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class SongController {
    private final SongService songService;
    @PostMapping("/create")
    public ResponseEntity<?> createSong(@RequestBody CreateSongRequestDto createSongRequestDto) {
        try {
            SongDto createSongDto = SongDto.builder()
                    .artist(createSongRequestDto.getArtist())
                    .title(createSongRequestDto.getTitle())
                    .releaseDate(createSongRequestDto.getReleaseDate())
                    .durationTime(createSongRequestDto.getDurationTime())
                    .album(createSongRequestDto.getAlbum())
                    .build();
            songService.createSong(createSongDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("SongEntity created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{title}/{artist}")
    public ResponseEntity<?> getSongByTitleAndArtist( @PathVariable String title,
                                                      @PathVariable String artist) {
        try {
            SongDto song = songService.getSongByTitleAndArtist(title, artist);
            SongResponseDto songResponseDto = SongResponseDto.builder()
                    .id(song.getId())
                    .title(song.getTitle())
                    .artist(song.getArtist())
                    .album(song.getAlbum())
                    .releaseDate(song.getReleaseDate())
                    .durationTime(song.getDurationTime())
                    .build();
            return ResponseEntity.ok(songResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{songId}") // 노래 제목과 아티스트만 반환해주는 거
    public ResponseEntity<?> getSongTitleAndArtist( @PathVariable Long songId) {
        try {
            log.info("song 여기");
            SongDto song = songService.getSongById(songId);
            log.info("song 여기2");
            SongResponseDto songResponseDto = SongResponseDto.builder()
                    .title(song.getTitle())
                    .artist(song.getArtist())
                    .build();
            log.info("song 여기3");
            return ResponseEntity.ok(songResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
