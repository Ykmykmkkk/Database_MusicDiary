package com.example.reviewservice.repository;


import com.example.reviewservice.domain.LikedReviewEntity;
import com.example.reviewservice.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikedReviewRepository extends JpaRepository<LikedReviewEntity, Long> {
    List<LikedReviewEntity> findAllByUserId(UUID userId);

    Optional<LikedReviewEntity> findByUserIdAndReviewEntity(UUID userId, ReviewEntity reviewEntity);

    Boolean findByUserIdAndReviewEntityId(UUID userId, Long reviewId);
}
