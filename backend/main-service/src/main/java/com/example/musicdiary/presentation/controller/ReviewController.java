package com.example.musicdiary.presentation.controller;

import com.example.musicdiary.common.ReviewDto;
import com.example.musicdiary.presentation.dto.request.CreateReviewRequestDto;
import com.example.musicdiary.presentation.dto.response.ReviewResponseDto;
import com.example.musicdiary.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestHeader("X-User-Id") Long userId, @RequestBody CreateReviewRequestDto createReviewRequestDto) {
        try {
            ReviewDto createReviewDto = modelMapper.map(createReviewRequestDto, ReviewDto.class);
            reviewService.createReview(userId, createReviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("reviewEntity created");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> getReviewDate(@RequestHeader("X-User-Id") Long userId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            ReviewDto reviewDto = reviewService.getReviewDate(userId, date);
            ReviewResponseDto reviewResponseDto = modelMapper.map(reviewDto, ReviewResponseDto.class);
            return ResponseEntity.ok(reviewResponseDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReview(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<ReviewDto> reviewDtoList = reviewService.getAllReview(userId);
            List<ReviewResponseDto> reviewResponseDtoList =
                    reviewDtoList.stream().map(reviewDto -> modelMapper.map(reviewDto,ReviewResponseDto.class)).toList();
            return ResponseEntity.ok(reviewResponseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/public") //
    public ResponseEntity<?> getPublicReview(@RequestHeader("X-User-Id") Long userId) {
        try {
            List<ReviewDto> reviewDtoList = reviewService.getPublicReview();
            List<ReviewResponseDto> reviewResponseDtoList =
                    reviewDtoList.stream().map(reviewDto -> modelMapper.map(reviewDto,ReviewResponseDto.class)).toList();
            return ResponseEntity.ok(reviewResponseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
