package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedReviewEntity;
import com.example.musicdiary.domain.ReviewEntity;
import com.example.musicdiary.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikedReviewRepository extends JpaRepository<LikedReviewEntity, Long> {
    List<LikedReviewEntity> findAllByUserEntityId(long userId);

    boolean existsByUserEntityUsernameAndReviewEntityReviewDate(String reviewWriter, LocalDate reviewDate);

    Optional<LikedReviewEntity> findByUserEntityAndReviewEntity(UserEntity userEntity, ReviewEntity reviewEntity);
}
