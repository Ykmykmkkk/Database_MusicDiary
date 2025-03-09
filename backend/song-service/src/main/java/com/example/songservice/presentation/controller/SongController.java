package com.example.songservice.presentation.controller;

import com.example.songservice.application.SongService;
import com.example.songservice.common.SongDto;
import com.example.songservice.presentation.dto.requestDto.CreateSongRequestDto;
import com.example.songservice.presentation.dto.responseDto.SongResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
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
}
