package com.example.reviewservice.application;

import com.example.reviewservice.common.ReviewDto;
import com.example.reviewservice.common.SongClient;
import com.example.reviewservice.common.SongResponseDto;
import com.example.reviewservice.common.UserClient;
import com.example.reviewservice.domain.ReviewEntity;
import com.example.reviewservice.repository.ReviewRepository;
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
        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No reviewEntity found");
        }
        return toReviewDtoList(userId, reviewEntities);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview(UUID userId, ReviewDto createReviewDto) {
        boolean isExists = reviewRepository.existsByWriterIdAndReviewDate(
                createReviewDto.getWriterId(),createReviewDto.getReviewDate());
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

        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No public reviewEntity found");
        }
        return toReviewDtoList(null,reviewEntities);
    }
    @Transactional(readOnly = true) // 사용자가 캘린더를 통해 해당 날짜에 자신이 작성한 리뷰를 받는 메소드
    public ReviewDto getReviewDate(UUID userId, LocalDate date) {
        ReviewEntity reviewEntity = reviewRepository.findByWriterIdAndReviewDate(userId, date).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
        log.info("서비스1:");
        ResponseEntity<String> userResponse = userClient.getUsername(userId);
        String writerUsername = userResponse.getBody();
        log.info("서비스2:");
        ResponseEntity<SongResponseDto> songResponse = songClient.getSongTitleAndArtist(reviewEntity.getSongId());
        SongResponseDto songData = songResponse.getBody();
        log.info("서비스3:");
        ResponseEntity<Boolean> songLikeResponse = songClient.isLikedSong(reviewEntity.getSongId());
        Boolean songLiked = songLikeResponse.getBody();
        log.info("서비스4:");
        Boolean reviewLiked = likedReviewService.isLike(userId, reviewEntity.getId());
        log.info("서비스5:");
        return  ReviewDto.builder()
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
                            Boolean reviewLiked = false;
                            if(userId!=null) reviewLiked=likedReviewService.isLike(userId, review.getId());
                            ResponseEntity<String> userResponse = userClient.getUsername(review.getWriterId());
                            String writerUsername = userResponse.getBody();

                            ResponseEntity<SongResponseDto> songResponse =
                                    songClient.getSongTitleAndArtist(review.getSongId());
                            SongResponseDto songData = songResponse.getBody();

                            ResponseEntity<Boolean> songLikeResponse = songClient.isLikedSong(review.getSongId());
                            Boolean songLiked = songLikeResponse.getBody();

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
                        }
                )
                .toList();
    }


    public ReviewEntity getReviewEntityById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
    }

}
