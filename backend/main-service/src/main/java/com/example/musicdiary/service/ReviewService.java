package com.example.musicdiary.service;

import com.example.musicdiary.common.ReviewDto;
import com.example.musicdiary.domain.ReviewEntity;
import com.example.musicdiary.domain.SongEntity;
import com.example.musicdiary.domain.UserEntity;
import com.example.musicdiary.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview(ReviewDto createReviewDto) {
        boolean isExists = reviewRepository.existsByUserEntity_usernameAndReviewDate(
                createReviewDto.getUsername(),createReviewDto.getReviewDate());
        if (isExists) {
           throw new IllegalArgumentException("Already reviewed");
        }
        String username = createReviewDto.getUsername();
        UserEntity userEntity = userService.getUserEntityByUsername(username);
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
    public List<ReviewDto> getAllReview(String username) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByUserEntity_username(username);
        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No reviewEntity found");
        }
        return toReviewDtoList(reviewEntities);
    }
    @Transactional(readOnly = true)
    public List<ReviewDto> getPublicReview() {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByIsPublicTrue();
        if(reviewEntities.isEmpty()){
            throw new RuntimeException("No public reviewEntity found");
        }
        return toReviewDtoList(reviewEntities);
    }
    @Transactional(readOnly = true)
    public ReviewDto getReviewDate(String username, LocalDate date) {
        ReviewEntity reviewEntity = reviewRepository.findByUserEntity_usernameAndReviewDate(username, date).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
        return modelMapper.map(reviewEntity,ReviewDto.class);
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

    public ReviewEntity getReviewEntityByUsernameAndReviewDate(String username, LocalDate date) {
        return reviewRepository.findByUserEntity_usernameAndReviewDate(username, date).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
    }

}
