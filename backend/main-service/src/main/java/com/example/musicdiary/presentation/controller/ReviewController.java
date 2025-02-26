package com.example.musicdiary.presentation.controller;

import com.example.musicdiary.common.ReviewDto;
import com.example.musicdiary.presentation.dto.request.CreateReviewRequestDto;
import com.example.musicdiary.presentation.dto.request.SetReviewLikeRequestDto;
import com.example.musicdiary.presentation.dto.response.ReviewResponseDto;
import com.example.musicdiary.service.LikedReviewService;
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
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequestDto createReviewRequestDto) {
        try {
            ReviewDto createReviewDto = modelMapper.map(createReviewRequestDto, ReviewDto.class);
            reviewService.createReview(createReviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("reviewEntity created");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> getReviewDate(@RequestHeader("username") String username, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            ReviewDto reviewDto = reviewService.getReviewDate(username,date);
            ReviewResponseDto reviewResponseDto = modelMapper.map(reviewDto, ReviewResponseDto.class);
            return ResponseEntity.ok(reviewResponseDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReview(@RequestHeader("X-User-Id") String username) {
        try {
            List<ReviewDto> reviewDtoList = reviewService.getAllReview(username);
            List<ReviewResponseDto> reviewResponseDtoList =
                    reviewDtoList.stream().map(reviewDto -> modelMapper.map(reviewDto,ReviewResponseDto.class)).toList();
            return ResponseEntity.ok(reviewResponseDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/public")
    public ResponseEntity<?> getPublicReview() {
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
