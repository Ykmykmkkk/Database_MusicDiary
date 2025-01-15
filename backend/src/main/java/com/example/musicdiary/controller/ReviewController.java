package com.example.musicdiary.controller;

import com.example.musicdiary.dto.RequestDTO.CreateReviewRequestDto;
import com.example.musicdiary.dto.RequestDTO.SetReviewLikeRequestDto;
import com.example.musicdiary.service.LikedReviewService;
import com.example.musicdiary.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final LikedReviewService likedReviewService;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequestDto createReviewRequestDto) {
        try {
            reviewService.createReview2(createReviewRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("review created");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{date}")
    public ResponseEntity<?> getReviewDate(@RequestHeader("username") String username, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            return ResponseEntity.ok(reviewService.getReviewDate(username,date));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReview(@RequestHeader("username") String username) {
        try {
            return ResponseEntity.ok(reviewService.getAllReview(username));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/public")
    public ResponseEntity<?> getPublicReview() {
        try {
            return ResponseEntity.ok(reviewService.getPublicReview());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/like")
    public ResponseEntity<?> setReviewLike(@RequestHeader("username") String username, @RequestBody SetReviewLikeRequestDto setReviewLikeRequestDto) {
        try {
            likedReviewService.setReviewLike(username,setReviewLikeRequestDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/unlike")
    public ResponseEntity<?> setReviewUnLike(@RequestHeader("username") String username, @RequestBody SetReviewLikeRequestDto setReviewLikeRequestDto) {
        try {
            likedReviewService.setReviewUnlike(username,setReviewLikeRequestDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/like")
    public ResponseEntity<?> getReviewLike(@RequestHeader("username") String username) {
        try {
            return ResponseEntity.ok(likedReviewService.getLikedReviewListByUsername(username));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
