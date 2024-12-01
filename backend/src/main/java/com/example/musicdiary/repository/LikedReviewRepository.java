package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedReviewRepository extends JpaRepository<LikedReview, Long> {
    @Query("SELECT lr.review FROM LikedReview lr WHERE lr.user.username = :username")
    List<LikedReview> findAllByUser_Username(String username);
}
