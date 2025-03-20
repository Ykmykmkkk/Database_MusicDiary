package com.example.reviewservice.application;


import com.example.reviewservice.common.ReviewDto;
import com.example.reviewservice.common.SongClient;
import com.example.reviewservice.common.SongResponseDto;
import com.example.reviewservice.common.UserClient;
import com.example.reviewservice.domain.LikedReviewEntity;
import com.example.reviewservice.domain.ReviewEntity;
import com.example.reviewservice.repository.LikedReviewRepository;
import com.example.reviewservice.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
@Slf4j
public class LikedReviewService {
    private final LikedReviewRepository likedReviewRepository;
    private final UserClient userClient;
    private final SongClient songClient;
    private final ReviewRepository reviewRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void likeReview(UUID userId, Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
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
                .map(review -> {
                            ResponseEntity<String> userResponse = userClient.getUsername(review.getWriterId());
                            String writerUsername = userResponse.getBody();

                            ResponseEntity<SongResponseDto> songResponse =
                                    songClient.getSongTitleAndArtist(review.getSongId());
                            SongResponseDto songData = songResponse.getBody();

                            ResponseEntity<Boolean> songLikeResponse = songClient.isLikedSong(review.getSongId());
                            Boolean songLiked = songLikeResponse.getBody();

                            return ReviewDto.builder()
                                    .writerUsername(writerUsername)
                                    .reviewDate(review.getReviewDate())
                                    .reviewTitle(review.getReviewTitle())
                                    .reviewContent(review.getReviewContent())
                                    .songId(review.getSongId())
                                    .songTitle(songData.getTitle())
                                    .songArtist(songData.getArtist())
                                    .songLiked(songLiked)
                                    .isPublic(review.getIsPublic())
                                    .reviewLiked(true)
                                    .build();
                        }
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public Boolean isLike(UUID userId, Long reviewId) {

        Boolean ha = likedReviewRepository.existsByUserIdAndReviewEntityId(userId,reviewId);
        log.info(ha+"");
        return ha;
    }

    @Transactional
    public void unlikeReview(UUID userId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
        LikedReviewEntity likedReviewEntity = likedReviewRepository.findByUserIdAndReviewEntity(userId, review)
                .orElseThrow(() -> new EntityNotFoundException("LikedSongEntity not found"));

        // 삭제 처리
        likedReviewRepository.delete(likedReviewEntity);
    }
}
