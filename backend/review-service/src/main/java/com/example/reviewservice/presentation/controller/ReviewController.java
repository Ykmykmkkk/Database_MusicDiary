package com.example.reviewservice.presentation.controller;


import com.example.reviewservice.application.LikedReviewService;
import com.example.reviewservice.application.ReviewService;
import com.example.reviewservice.common.ReviewDto;
import com.example.reviewservice.domain.ReviewEntity;
import com.example.reviewservice.presentation.dto.requestDto.CreateReviewRequestDto;
import com.example.reviewservice.presentation.dto.responseDto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
    private final LikedReviewService likedReviewService;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestHeader("X-User-Id") UUID userId, @RequestBody CreateReviewRequestDto createReviewRequestDto) {
        try {
            ReviewDto createReviewDto =  ReviewDto.builder()
                    .reviewDate(createReviewRequestDto.getReviewDate())
                    .writerId(createReviewRequestDto.getWriterId())
                    .songId(createReviewRequestDto.getSongId())
                    .reviewTitle(createReviewRequestDto.getReviewTitle())
                    .reviewContent(createReviewRequestDto.getReviewContent())
                    .isPublic(createReviewRequestDto.getIsPublic())
                    .build();

            reviewService.createReview(userId, createReviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("reviewEntity created");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> getReviewDate(@RequestHeader("X-User-Id") UUID userId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            log.info(userId + "|"+ date);
            log.info("컨트롤러: 여기까지");
            ReviewDto reviewDto = reviewService.getReviewDate(userId, date);
            log.info("컨트롤러: 여기까지2");
            Boolean reviewLike = likedReviewService.isLike(userId, reviewDto.getId());
            log.info("컨트롤러: 여기까지3");
            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .reviewId(reviewDto.getId())
                    .reviewDate(reviewDto.getReviewDate())
                    .songId(reviewDto.getSongId())
                    .songArtist(reviewDto.getSongArtist())
                    .songTitle(reviewDto.getSongTitle())
                    .songLiked(reviewDto.getSongLiked())
                    .reviewTitle(reviewDto.getReviewTitle())
                    .reviewContent(reviewDto.getReviewContent())
                    .isPublic(reviewDto.getIsPublic())
                    .reviewLiked(reviewLike)
                    .build();
            log.info("컨트롤러: 여기까지4");
            return ResponseEntity.ok(reviewResponseDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReview(@RequestHeader("X-User-Id") UUID userId) {
        try {
            List<ReviewDto> reviewDtoList = reviewService.getAllReview(userId);
            List<ReviewResponseDto> reviewResponseDtoList =
                    reviewDtoList.stream().map(reviewDto -> ReviewResponseDto.builder()
                            .reviewId(reviewDto.getId())
                            .reviewDate(reviewDto.getReviewDate())
                            .songId(reviewDto.getSongId())
                            .songArtist(reviewDto.getSongArtist())
                            .songTitle(reviewDto.getSongTitle())
                            .songLiked(reviewDto.getSongLiked())
                            .reviewTitle(reviewDto.getReviewTitle())
                            .reviewContent(reviewDto.getReviewContent())
                            .reviewLiked(likedReviewService.isLike(userId, reviewDto.getId()))
                            .build()
                    ).toList();
            return ResponseEntity.ok(reviewResponseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/public") //
    public ResponseEntity<?> getPublicReview(@RequestHeader("X-User-Id") UUID userId) {
        try {
            List<ReviewDto> reviewDtoList = reviewService.getPublicReview();
            List<ReviewResponseDto> reviewResponseDtoList =
                    reviewDtoList.stream().map(reviewDto -> ReviewResponseDto.builder()
                            .reviewId(reviewDto.getId())
                            .reviewDate(reviewDto.getReviewDate())
                            .songId(reviewDto.getSongId())
                            .songArtist(reviewDto.getSongArtist())
                            .songTitle(reviewDto.getSongTitle())
                            .songLiked(reviewDto.getSongLiked())
                            .reviewTitle(reviewDto.getReviewTitle())
                            .reviewContent(reviewDto.getReviewContent())
                            .isPublic(reviewDto.getIsPublic())
                            .reviewLiked(likedReviewService.isLike(userId, reviewDto.getId()))
                            .build()
                    ).toList();
            return ResponseEntity.ok(reviewResponseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
