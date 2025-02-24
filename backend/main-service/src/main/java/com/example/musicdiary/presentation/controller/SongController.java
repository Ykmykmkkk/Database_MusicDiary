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
    private final LikedSongService likedSongService;
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

    @PostMapping("/like")
    public ResponseEntity<?> setSongLike(@RequestHeader("username") String username, @RequestBody SetSongLikeRequestDto setSongLikeRequestDto) {
        try {
            SongDto songDto = modelMapper.map(setSongLikeRequestDto,SongDto.class);
            likedSongService.setSongLike(username,songDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> setSongUnlike(@RequestHeader("username") String username, @RequestBody SetSongLikeRequestDto setSongLikeRequestDto) {
        try {
            SongDto songDto = modelMapper.map(setSongLikeRequestDto,SongDto.class);
            likedSongService.setSongUnlike(username,songDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/like")
    public ResponseEntity<?> getSongLike(@RequestHeader("username") String username) {
        try {
            List<SongDto> likedSongList = likedSongService.getLikedSongListByUsername(username);
            List<SongResponseDto> responseDtoList = likedSongList.stream().map(songDto -> modelMapper.map(songDto,SongResponseDto.class)).toList();
            return ResponseEntity.ok(responseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
