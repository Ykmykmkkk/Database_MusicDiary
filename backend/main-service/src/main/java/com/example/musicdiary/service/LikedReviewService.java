package com.example.musicdiary.service;

import com.example.musicdiary.common.ReviewDto;
import com.example.musicdiary.domain.LikedReviewEntity;
import com.example.musicdiary.domain.ReviewEntity;
import com.example.musicdiary.domain.UserEntity;
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
    public void setReviewLike(Long userId, ReviewDto setReviewLikeDto) {
        boolean isExist = likedReviewRepository.existsByUserEntityUsernameAndReviewEntityReviewDate(
                setReviewLikeDto.getUsername(), setReviewLikeDto.getReviewDate());
        if (isExist) {

            throw new IllegalArgumentException("Already liked this reviewEntity");
        }
        UserEntity userEntity = userService.getUserEntityByUserId(userId);
        String reviewWriter = setReviewLikeDto.getUsername();
        LocalDate date = setReviewLikeDto.getReviewDate();
        ReviewEntity reviewEntity = reviewService.getReviewEntityByWriterNameAndReviewDate(reviewWriter, date);
        LikedReviewEntity likedReviewEntity = LikedReviewEntity.builder()
                .userEntity(userEntity)
                .reviewEntity(reviewEntity)
                .build();
        likedReviewRepository.save(likedReviewEntity);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getLikedReviewListByUserId(long userId) {
        List<ReviewEntity> likedReviewEntities = likedReviewRepository.findAllByUserEntityId(userId).stream()
                .map(LikedReviewEntity::getReviewEntity)
                .toList();
        return toResponseDtoList(likedReviewEntities);
    }

    public List<ReviewDto> toResponseDtoList(List<ReviewEntity> reviewEntities) {
        return reviewEntities.stream()
                .map(review -> ReviewDto.builder()
                        .username(review.getUserEntity().getUsername())
                        .reviewDate(review.getReviewDate())
                        .reviewContent(review.getReviewContent())
                        .songTitle(review.getSongEntity().getTitle())
                        .songArtist(review.getSongEntity().getArtist())
                        .isPublic(review.getIsPublic())
                        .isLike(true)
                        .build()).toList();
    }

    @Transactional
    public void setReviewUnlike(Long userId, ReviewDto setReviewLikeDto) {
        UserEntity userEntity = userService.getUserEntityByUserId(userId);
        String reviewWriter = setReviewLikeDto.getUsername();
        LocalDate date = setReviewLikeDto.getReviewDate();
        ReviewEntity reviewEntity = reviewService.getReviewEntityByWriterNameAndReviewDate(reviewWriter, date);

        // LikedReviewEntity 엔터티 조회
        LikedReviewEntity likedReviewEntity = likedReviewRepository.findByUserEntityAndReviewEntity(userEntity, reviewEntity)
                .orElseThrow(() -> new EntityNotFoundException("LikedReviewEntity not found"));

        // 삭제 처리
        likedReviewRepository.delete(likedReviewEntity);
    }
}