package com.example.musicdiary.service;

import com.example.musicdiary.domain.LikedReview;
import com.example.musicdiary.domain.Review;
import com.example.musicdiary.domain.Song;
import com.example.musicdiary.domain.User;
import com.example.musicdiary.dto.RequestDTO.SetReviewLikeRequestDto;
import com.example.musicdiary.repository.LikedReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikedReviewService {
    private final LikedReviewRepository likedReviewRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void setReviewLike(String username, SetReviewLikeRequestDto setReviewLikeRequestDto) {
        User user = userService.getUserEntityByUsername(username);
        Long reviewId = setReviewLikeRequestDto.getReviewId();
        Review review = reviewService.getReviewEntityById(reviewId);
        LikedReview likedReview = LikedReview.builder()
               .user(user)
               .review(review)
               .build();
        likedReviewRepository.save(likedReview);
    }
    public List<Review> getLikedReviewListByUsername(String username) {
        return likedReviewRepository.findAllByUser_Username(username).stream()
                .map(LikedReview::getReview)
                .toList();
    }

}
