package com.example.reviewservice.application;

import com.example.reviewservice.common.ReviewDto;
import com.example.reviewservice.common.SongClient;
import com.example.reviewservice.common.SongResponseDto;
import com.example.reviewservice.common.UserClient;
import com.example.reviewservice.domain.ReviewEntity;
import com.example.reviewservice.repository.ReviewRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserClient userClient;
    private final SongClient songClient;
    private final LikedReviewService likedReviewService;

    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReview(UUID userId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByWriterId(userId);
        if (reviewEntities.isEmpty()) {
            throw new RuntimeException("No reviewEntity found");
        }
        return toReviewDtoList(userId, reviewEntities);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview(UUID userId, ReviewDto createReviewDto) {
        boolean isExists = reviewRepository.existsByWriterIdAndReviewDate(
                createReviewDto.getWriterId(), createReviewDto.getReviewDate());

        if (isExists) {
            throw new IllegalArgumentException("Already reviewed");
        }

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .reviewDate(createReviewDto.getReviewDate())
                .writerId(userId)
                .songId(createReviewDto.getSongId())
                .reviewTitle(createReviewDto.getReviewTitle())
                .reviewContent(createReviewDto.getReviewContent())
                .isPublic(createReviewDto.getIsPublic())
                .build();

        reviewRepository.save(reviewEntity);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getPublicReview() {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByIsPublicTrue();
        if (reviewEntities.isEmpty()) {
            throw new RuntimeException("No public reviewEntity found");
        }
        return toReviewDtoList(null, reviewEntities);
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDate(UUID userId, LocalDate date) {
        ReviewEntity reviewEntity = reviewRepository.findByWriterIdAndReviewDate(userId, date)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));

        // 1. UserClient 호출 (Fallback: "Unknown User")
        String writerUsername = getUsernameByUserClient(userId);

        // 2. SongClient - SongTitleAndArtist 호출 (Fallback: "Unknown Title", "Unknown Artist")
        SongResponseDto songData = getSongTitleAndArtistBySongClient(reviewEntity.getSongId());

        // 3. SongClient - isLikedSong 호출 (Fallback: false)
        Boolean songLiked = isLikedSongBySongClient(reviewEntity.getSongId());

        // 4. LikedReviewService 호출 (로컬 서비스라 서킷 브레이커 필요 없음)
        Boolean reviewLiked = likedReviewService.isLike(userId, reviewEntity.getId());

        return ReviewDto.builder()
                .id(reviewEntity.getId())
                .reviewDate(reviewEntity.getReviewDate())
                .writerId(reviewEntity.getWriterId())
                .writerUsername(writerUsername)
                .songId(reviewEntity.getSongId())
                .songTitle(songData.getTitle())
                .songArtist(songData.getArtist())
                .songLiked(songLiked)
                .reviewTitle(reviewEntity.getReviewTitle())
                .reviewContent(reviewEntity.getReviewContent())
                .reviewLiked(reviewLiked)
                .isPublic(reviewEntity.getIsPublic())
                .build();
    }

    public List<ReviewDto> toReviewDtoList(UUID userId, List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream()
                .map(review -> {
                    Boolean reviewLiked = (userId != null) && likedReviewService.isLike(userId, review.getId());
                    String writerUsername = getUsernameByUserClient(review.getWriterId());
                    SongResponseDto songData = getSongTitleAndArtistBySongClient(review.getSongId());
                    Boolean songLiked = isLikedSongBySongClient(review.getSongId());

                    return ReviewDto.builder()
                            .id(review.getId())
                            .reviewDate(review.getReviewDate())
                            .writerId(review.getWriterId())
                            .writerUsername(writerUsername)
                            .songId(review.getSongId())
                            .songTitle(songData.getTitle())
                            .songArtist(songData.getArtist())
                            .songLiked(songLiked)
                            .reviewTitle(review.getReviewTitle())
                            .reviewContent(review.getReviewContent())
                            .isPublic(review.getIsPublic())
                            .reviewLiked(reviewLiked)
                            .build();
                })
                .toList();
    }

    public ReviewEntity getReviewEntityById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
    }

    @CircuitBreaker(name = "circuitbreaker", fallbackMethod = "getUsernameFallback")
    public String getUsernameByUserClient(UUID userId) {
        return userClient.getUsername(userId).getBody();
    }

    public String getUsernameFallback(UUID userId, Throwable t) {
        log.error("UserClient 호출 실패: {}", t.getMessage());
        return "Unknown User";
    }

    @CircuitBreaker(name = "circuitbreaker", fallbackMethod = "getSongTitleAndArtistFallback")
    public SongResponseDto getSongTitleAndArtistBySongClient(Long songId) {
        return songClient.getSongTitleAndArtist(songId).getBody();
    }

    public SongResponseDto getSongTitleAndArtistFallback(Long songId, Throwable t) {
        log.error("SongClient SongTitleAndArtist 호출 실패: {}", t.getMessage());
        return new SongResponseDto("Unknown Title", "Unknown Artist");
    }
    @CircuitBreaker(name = "circuitbreaker", fallbackMethod = "isLikedSongFallback")
    public Boolean isLikedSongBySongClient(Long songId) {
        return songClient.isLikedSong(songId).getBody();
    }

    public Boolean isLikedSongFallback(Long songId, Throwable t) {
        log.error("SongClient isLikedSong 호출 실패: {}", t.getMessage());
        return false;
    }
}
