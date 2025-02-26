package com.example.musicdiary.presentation.controller;


import com.example.musicdiary.common.SongDto;
import com.example.musicdiary.presentation.dto.request.CreateSongRequestDto;
import com.example.musicdiary.presentation.dto.request.SetSongLikeRequestDto;
import com.example.musicdiary.presentation.dto.response.SongResponseDto;
import com.example.musicdiary.service.LikedSongService;
import com.example.musicdiary.service.SongService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {
    private final SongService songService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createSong(@RequestBody CreateSongRequestDto createSongRequestDto) {
        try {
            SongDto createSongDto = modelMapper.map(createSongRequestDto, SongDto.class);
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
            SongResponseDto songResponseDto = modelMapper.map(song, SongResponseDto.class);
            return ResponseEntity.ok(songResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
