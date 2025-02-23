package com.example.musicdiary.service;

import com.example.musicdiary.domain.ReviewEntity;
import com.example.musicdiary.domain.SongEntity;
import com.example.musicdiary.domain.UserEntity;
import com.example.musicdiary.presentation.dto.request.CreateReviewRequestDto;
import com.example.musicdiary.presentation.dto.response.ReviewResponseDto;
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview(CreateReviewRequestDto createReviewRequestDto) {
        boolean isExists = reviewRepository.existsByUserEntity_usernameAndReviewDate(
                createReviewRequestDto.getUsername(),createReviewRequestDto.getReviewDate());
        if (isExists) {
           throw new IllegalArgumentException("Already reviewed");
        }
        String username = createReviewRequestDto.getUsername();
        UserEntity userEntity = userService.getUserEntityByUsername(username);
        String songTitle = createReviewRequestDto.getSongTitle();
        String songArtist = createReviewRequestDto.getSongArtist();
        SongEntity songEntity = songService.getSongEntityByTitleAndArtist(songTitle, songArtist);
        ReviewEntity reviewEntity = ReviewEntity.builder()
            .userEntity(userEntity)
            .songEntity(songEntity)
            .reviewContent(createReviewRequestDto.getReviewContent())
            .reviewDate(createReviewRequestDto.getReviewDate())
            .isPublic(createReviewRequestDto.getIsPublic())
            .build();
        reviewRepository.save(reviewEntity);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview2(CreateReviewRequestDto createReviewRequestDto) {
        reviewRepository.createReviewbyNativequery(
                createReviewRequestDto.getIsPublic(),
                createReviewRequestDto.getReviewContent(),
                createReviewRequestDto.getReviewDate(),
                createReviewRequestDto.getSongTitle(),
                createReviewRequestDto.getSongArtist(),
                createReviewRequestDto.getUsername()
        );
    }


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReview(String username) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByUserEntity_username(username);
        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No reviewEntity found");
        }
        return toResponseDtoList(reviewEntities);
    }
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getPublicReview() {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByIsPublicTrue();
        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No public reviewEntity found");
        }
        return toResponseDtoList(reviewEntities);
    }
    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewDate(String username, LocalDate date) {
        ReviewEntity reviewEntity = reviewRepository.findByUserEntity_usernameAndReviewDate(username, date).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
        return ReviewResponseDto.builder()
               .username(reviewEntity.getUserEntity().getUsername())
               .title(reviewEntity.getSongEntity().getTitle())
               .artist(reviewEntity.getSongEntity().getArtist())
               .reviewContent(reviewEntity.getReviewContent())
               .reviewDate(reviewEntity.getReviewDate())
               .isPublic(reviewEntity.getIsPublic())
               .build();
    }

    public List<ReviewResponseDto> toResponseDtoList(List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream()
               .map(review -> ReviewResponseDto.builder()
                       .username(review.getUserEntity().getUsername())
                       .title(review.getSongEntity().getTitle())
                       .artist(review.getSongEntity().getArtist())
                       .reviewContent(review.getReviewContent())
                       .reviewDate(review.getReviewDate())
                       .isPublic(review.getIsPublic())
                       .build())
               .toList();
    }

    public ReviewEntity getReviewEntityByUsernameAndReviewDate(String username, LocalDate date) {
        return reviewRepository.findByUserEntity_usernameAndReviewDate(username, date).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
    }

}
