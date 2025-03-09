package com.example.reviewservice.repository;

import com.example.reviewservice.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findAllByIsPublicTrue();
    List<ReviewEntity> findAllByWriterId(UUID userId);


    Optional<ReviewEntity> findByWriterIdAndReviewDate(UUID userId, LocalDate date);

    boolean existsByWriterUsernameAndReviewDate(String username, LocalDate reviewDate);

}
