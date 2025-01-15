package com.example.musicdiary.service;

import com.example.musicdiary.domain.LikedReview;
import com.example.musicdiary.domain.Review;
import com.example.musicdiary.domain.Song;
import com.example.musicdiary.domain.User;
import com.example.musicdiary.dto.RequestDTO.SetReviewLikeRequestDto;
import com.example.musicdiary.dto.ResponseDto.ReviewResponseDto;
import com.example.musicdiary.dto.ResponseDto.SongResponseDto;
import com.example.musicdiary.repository.LikedReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LikedReviewService {
    private final LikedReviewRepository likedReviewRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void setReviewLike(String username, SetReviewLikeRequestDto setReviewLikeRequestDto) {
        boolean isExist = likedReviewRepository.existsByUser_UsernameAndReview_ReviewDate(setReviewLikeRequestDto.getReviewWriter(),setReviewLikeRequestDto.getReviewDate());
        if(isExist){
            throw new IllegalArgumentException("Already liked this review");
        }
        User user = userService.getUserEntityByUsername(username);
        String reviewWriter = setReviewLikeRequestDto.getReviewWriter();
        LocalDate date = setReviewLikeRequestDto.getReviewDate();
        Review review = reviewService.getReviewEntityByUsernameAndReviewDate(reviewWriter, date);
        LikedReview likedReview = LikedReview.builder()
               .user(user)
               .review(review)
               .build();
        likedReviewRepository.save(likedReview);
    }
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getLikedReviewListByUsername(String username) {
        List<Review> likedReviews= likedReviewRepository.findAllByUser_Username(username).stream()
                .map(LikedReview::getReview)
                .toList();
        return toResponseDtoList(likedReviews);
    }
    public List<ReviewResponseDto> toResponseDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(review -> ReviewResponseDto.builder()
                        .username(review.getUser().getUsername())
                        .reviewDate(review.getReviewDate())
                        .reviewContent(review.getReviewContent())
                        .title(review.getSong().getTitle())
                        .artist(review.getSong().getArtist())
                        .isPublic(review.getIsPublic())
                .build()).toList();
    }
    @Transactional
    public void setReviewUnlike(String username, SetReviewLikeRequestDto setReviewLikeRequestDto) {
        User user = userService.getUserEntityByUsername(username);
        String reviewWriter = setReviewLikeRequestDto.getReviewWriter();
        LocalDate date = setReviewLikeRequestDto.getReviewDate();
        Review review = reviewService.getReviewEntityByUsernameAndReviewDate(reviewWriter, date);

        // LikedReview 엔터티 조회
        LikedReview likedReview = likedReviewRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new EntityNotFoundException("LikedReview not found"));

        // 삭제 처리
        likedReviewRepository.delete(likedReview);
    }

}
