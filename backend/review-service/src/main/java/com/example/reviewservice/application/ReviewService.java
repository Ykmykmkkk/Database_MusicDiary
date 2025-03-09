package com.example.reviewservice.application;

import com.example.reviewservice.common.ReviewDto;
import com.example.reviewservice.domain.ReviewEntity;
import com.example.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReview(UUID userId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByWriterId(userId);
        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No reviewEntity found");
        }
        return toReviewDtoList(reviewEntities);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview(UUID userId, ReviewDto createReviewDto) {
        boolean isExists = reviewRepository.existsByWriterUsernameAndReviewDate(
                createReviewDto.getWriterUsername(),createReviewDto.getReviewDate());
        if (isExists) {
            throw new IllegalArgumentException("Already reviewed");
        }
        Long songId = createReviewDto.getSongId();
        String songTitle = createReviewDto.getSongTitle();
        String songArtist = createReviewDto.getSongArtist();
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .reviewDate(createReviewDto.getReviewDate())
                .writerId(userId)
                .writerUsername(createReviewDto.getWriterUsername())
                .songId(songId)
                .songTitle(songTitle)
                .songArtist(songArtist)
                .reviewTitle(createReviewDto.getReviewTitle())
                .reviewContent(createReviewDto.getReviewContent())
                .isPublic(createReviewDto.getIsPublic())
                .build();
        reviewRepository.save(reviewEntity);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getPublicReview() {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByIsPublicTrue();

        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No public reviewEntity found");
        }
        return toReviewDtoList(reviewEntities);
    }
    @Transactional(readOnly = true) // 사용자가 캘린더를 통해 해당 날짜에 자신이 작성한 리뷰를 받는 메소드
    public ReviewDto getReviewDate(UUID userId, LocalDate date) {
        ReviewEntity reviewEntity = reviewRepository.findByWriterIdAndReviewDate(userId, date).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));

        return  ReviewDto.builder()
                .id(reviewEntity.getId())
                .reviewDate(reviewEntity.getReviewDate())
                .writerId(reviewEntity.getWriterId())
                .writerUsername(reviewEntity.getWriterUsername())
                .songId(reviewEntity.getSongId())
                .songTitle(reviewEntity.getSongTitle())
                .songArtist(reviewEntity.getSongArtist())
                .reviewTitle(reviewEntity.getReviewTitle())
                .reviewContent(reviewEntity.getReviewContent())
                .isPublic(reviewEntity.getIsPublic())
                .build();
    }

    public List<ReviewDto> toReviewDtoList(List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream()
                .map(review -> ReviewDto.builder()
                        .id(review.getId())
                        .reviewDate(review.getReviewDate())
                        .writerId(review.getWriterId())
                        .writerUsername(review.getWriterUsername())
                        .songId(review.getSongId())
                        .songTitle(review.getSongTitle())
                        .songArtist(review.getSongArtist())
                        .reviewTitle(review.getReviewTitle())
                        .reviewContent(review.getReviewContent())
                        .isPublic(review.getIsPublic())
                        .build())
                .toList();
    }


    public ReviewEntity getReviewEntityById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
    }

}
