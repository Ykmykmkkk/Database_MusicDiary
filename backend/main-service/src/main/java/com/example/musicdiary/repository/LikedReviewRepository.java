package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedReviewEntity;
import com.example.musicdiary.domain.ReviewEntity;
import com.example.musicdiary.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikedReviewRepository extends JpaRepository<LikedReviewEntity, Long> {
    @Query("SELECT lr FROM LikedReviewEntity lr WHERE lr.userEntity.username = :username")
    List<LikedReviewEntity> findAllByUser_Username(String username);

    boolean existsByUserEntity_UsernameAndReviewEntity_ReviewDate(String reviewWriter, LocalDate reviewDate);

    Optional<LikedReviewEntity> findByUserEntityAndReviewEntity(UserEntity userEntity, ReviewEntity reviewEntity);
}
