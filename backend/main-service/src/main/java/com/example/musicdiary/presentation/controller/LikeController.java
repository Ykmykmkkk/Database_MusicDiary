package com.example.musicdiary.presentation.controller;

import com.example.musicdiary.common.ReviewDto;
import com.example.musicdiary.common.SongDto;
import com.example.musicdiary.presentation.dto.request.SetReviewLikeRequestDto;
import com.example.musicdiary.presentation.dto.request.SetSongLikeRequestDto;
import com.example.musicdiary.presentation.dto.response.ReviewResponseDto;
import com.example.musicdiary.presentation.dto.response.SongResponseDto;
import com.example.musicdiary.service.LikedReviewService;
import com.example.musicdiary.service.LikedSongService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/like")
public class LikeController {
    private final ModelMapper modelMapper;
    private final LikedSongService likedSongService;
    private final LikedReviewService likedReviewService;

    @PostMapping("/song")
    public ResponseEntity<?> setSongLike(@RequestHeader("X-User-Id") String userId, @RequestBody SetSongLikeRequestDto setSongLikeRequestDto) {
        try {
            SongDto songDto = modelMapper.map(setSongLikeRequestDto,SongDto.class);
            likedSongService.setSongLike(userId,songDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/song/cancel")
    public ResponseEntity<?> setSongUnlike(@RequestHeader("X-User-Id") String userId, @RequestBody SetSongLikeRequestDto setSongLikeRequestDto) {
        try {
            SongDto songDto = modelMapper.map(setSongLikeRequestDto,SongDto.class);
            likedSongService.setSongUnlike(userId,songDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/song")
    public ResponseEntity<?> getSongLike(@RequestHeader("X-User-Id") String userId) {
        try {
            List<SongDto> likedSongList = likedSongService.getLikedSongListByUsername(userId);
            List<SongResponseDto> responseDtoList = likedSongList.stream().map(songDto -> modelMapper.map(songDto,SongResponseDto.class)).toList();
            return ResponseEntity.ok(responseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/review")
    public ResponseEntity<?> setReviewLike(@RequestHeader("X-User-Id") String userId, @RequestBody SetReviewLikeRequestDto setReviewLikeRequestDto) {
        try {
            ReviewDto reviewLikeDto = modelMapper.map(setReviewLikeRequestDto, ReviewDto.class);
            likedReviewService.setReviewLike(userId, reviewLikeDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/review/cancel")
    public ResponseEntity<?> setReviewUnLike(@RequestHeader("X-User-Id") String userId, @RequestBody SetReviewLikeRequestDto setReviewLikeRequestDto) {
        try {
            ReviewDto reviewUnlikeDto = modelMapper.map(setReviewLikeRequestDto, ReviewDto.class);
            likedReviewService.setReviewUnlike(userId, reviewUnlikeDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/review")
    public ResponseEntity<?> getReviewLike(@RequestHeader("X-User-Id") String userId) {
        try {
            List<ReviewDto> likedReviewDtoList = likedReviewService.getLikedReviewListByUsername(userId);
            List<ReviewResponseDto> reviewResponseDtoList = likedReviewDtoList.stream()
                    .map(reviewDto -> modelMapper.map(reviewDto, ReviewResponseDto.class)).toList();

            return ResponseEntity.ok(reviewResponseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
