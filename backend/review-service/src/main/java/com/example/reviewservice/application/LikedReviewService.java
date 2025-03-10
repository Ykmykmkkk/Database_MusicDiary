package com.example.reviewservice.application;


import com.example.reviewservice.common.ReviewDto;
import com.example.reviewservice.domain.LikedReviewEntity;
import com.example.reviewservice.domain.ReviewEntity;
import com.example.reviewservice.repository.LikedReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class LikedReviewService {
    private final LikedReviewRepository likedReviewRepository;
    private final ReviewService reviewService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void likeReview(UUID userId, Long reviewId) {
        ReviewEntity reviewEntity = reviewService.getReviewEntityById(reviewId);
        LikedReviewEntity likedReviewEntity = LikedReviewEntity.builder()
                .userId(userId)
                .reviewEntity(reviewEntity)
                .build();
        likedReviewRepository.save(likedReviewEntity);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getLikedReviewListByUserId(UUID userId) {
        List<ReviewEntity> likedReviewEntities = likedReviewRepository.findAllByUserId(userId).stream()
                .map(LikedReviewEntity::getReviewEntity)
                .toList();
        return toResponseDtoList(likedReviewEntities);
    }

    public List<ReviewDto> toResponseDtoList(List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream()
                .map(review -> ReviewDto.builder()
                        .writerUsername(review.getWriterUsername())
                        .reviewDate(review.getReviewDate())
                        .reviewTitle(review.getReviewTitle())
                        .reviewContent(review.getReviewContent())
                        .songId(review.getSongId())
                        .songTitle(review.getSongTitle())
                        .songArtist(review.getSongArtist())
                        .isPublic(review.getIsPublic())
                        .isLike(true)
                        .build()).toList();
    }

    @Transactional(readOnly = true)
    public Boolean isLike(UUID userId, Long reviewId) {
        return likedReviewRepository.findByUserIdAndReviewEntityId(userId,reviewId);
    }

    @Transactional
    public void unlikeReview(UUID userId, Long reviewId) {
        ReviewEntity review = reviewService.getReviewEntityById(reviewId);
        LikedReviewEntity likedReviewEntity = likedReviewRepository.findByUserIdAndReviewEntity(userId, review)
                .orElseThrow(() -> new EntityNotFoundException("LikedSongEntity not found"));

        // 삭제 처리
        likedReviewRepository.delete(likedReviewEntity);
    }
}
