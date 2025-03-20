package com.example.reviewservice.presentation.controller;

import com.example.reviewservice.application.LikedReviewService;
import com.example.reviewservice.common.ReviewDto;
import com.example.reviewservice.presentation.dto.responseDto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/")
public class LikedReviewController {
    private final LikedReviewService likedReviewService;
    @PostMapping("/like/{reviewId}")
    public ResponseEntity<?> setReviewLike(@RequestHeader("X-User-Id") UUID userId, @PathVariable Long reviewId) {
        try {
            likedReviewService.likeReview(userId, reviewId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/like/{reviewId}")
    public ResponseEntity<?> setReviewUnLike(@RequestHeader("X-User-Id") UUID userId, @PathVariable Long reviewId) {

        try {
            likedReviewService.unlikeReview(userId, reviewId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/like/all")
    public ResponseEntity<?> getReviewLike(@RequestHeader("X-User-Id") UUID userId) {
        try {
            log.info("여긴왔다");
            List<ReviewDto> likedReviewDtoList = likedReviewService.getLikedReviewListByUserId(userId);
            List<ReviewResponseDto> reviewResponseDtoList = likedReviewDtoList.stream()
                    .map(reviewDto -> ReviewResponseDto.builder()
                            .reviewId(reviewDto.getId())
                            .reviewDate(reviewDto.getReviewDate())
                            .songId(reviewDto.getSongId())
                            .songArtist(reviewDto.getSongArtist())
                            .songTitle(reviewDto.getSongTitle())
                            .songLiked(reviewDto.getSongLiked())
                            .reviewTitle(reviewDto.getReviewTitle())
                            .reviewContent(reviewDto.getReviewContent())
                            .isPublic(reviewDto.getIsPublic())
                            .reviewLiked(true)
                            .build()
                    ).toList();
            log.info("여긴왔다2");
            return ResponseEntity.ok(reviewResponseDtoList);
        }
        catch (Exception e) {
            log.info("여긴왔다3");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
