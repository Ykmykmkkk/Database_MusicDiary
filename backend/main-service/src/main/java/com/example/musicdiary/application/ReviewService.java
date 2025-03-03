package com.example.musicdiary.application;

import com.example.musicdiary.common.ReviewDto;
import com.example.musicdiary.domain.ReviewEntity;
import com.example.musicdiary.domain.SongEntity;
import com.example.musicdiary.domain.UserEntity;
import com.example.musicdiary.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final SongService songService;
    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReview(Long userId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByUserEntityId(userId);
        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No reviewEntity found");
        }
        return toReviewDtoList(reviewEntities);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview(Long userId, ReviewDto createReviewDto) {
        boolean isExists = reviewRepository.existsByUserEntity_usernameAndReviewDate(
                createReviewDto.getUsername(),createReviewDto.getReviewDate());
        if (isExists) {
           throw new IllegalArgumentException("Already reviewed");
        }
        UserEntity userEntity = userService.getUserEntityByUserId(userId);
        String songTitle = createReviewDto.getSongTitle();
        String songArtist = createReviewDto.getSongArtist();
        SongEntity songEntity = songService.getSongEntityByTitleAndArtist(songTitle, songArtist);
        ReviewEntity reviewEntity = ReviewEntity.builder()
            .userEntity(userEntity)
            .songEntity(songEntity)
            .reviewContent(createReviewDto.getReviewContent())
            .reviewDate(createReviewDto.getReviewDate())
            .isPublic(createReviewDto.getIsPublic())
            .build();
        reviewRepository.save(reviewEntity);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview2(ReviewDto createReviewDto) {
        reviewRepository.createReviewbyNativequery(
                createReviewDto.getIsPublic(),
                createReviewDto.getReviewContent(),
                createReviewDto.getReviewDate(),
                createReviewDto.getSongTitle(),
                createReviewDto.getSongArtist(),
                createReviewDto.getUsername()
        );
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
    public ReviewDto getReviewDate(Long userId, LocalDate date) {
        ReviewEntity reviewEntity = reviewRepository.findByUserEntityIdAndReviewDate(userId, date).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));

        return  ReviewDto.builder()
                .id(reviewEntity.getId())
                .reviewDate(reviewEntity.getReviewDate())
                .userId(reviewEntity.getUserEntity().getId())
                .username(reviewEntity.getUserEntity().getUsername())
                .songId(reviewEntity.getSongEntity().getId())
                .songTitle(reviewEntity.getSongEntity().getTitle())
                .songArtist(reviewEntity.getSongEntity().getArtist())
                .reviewContent(reviewEntity.getReviewContent())
                .isPublic(reviewEntity.getIsPublic())
                .build();
    }

    public List<ReviewDto> toReviewDtoList(List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream()
               .map(review -> ReviewDto.builder()
                       .username(review.getUserEntity().getUsername())
                       .songTitle(review.getSongEntity().getTitle())
                       .songArtist(review.getSongEntity().getArtist())
                       .reviewContent(review.getReviewContent())
                       .reviewDate(review.getReviewDate())
                       .isPublic(review.getIsPublic())
                       .build())
               .toList();
    }


    public ReviewEntity getReviewEntityByWriterNameAndReviewDate(String writerName, LocalDate date) {
        return reviewRepository.findByUserEntity_UsernameAndReviewDate(writerName, date).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
    }

}
