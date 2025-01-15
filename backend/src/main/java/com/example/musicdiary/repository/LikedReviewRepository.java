package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedReview;
import com.example.musicdiary.domain.Review;
import com.example.musicdiary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikedReviewRepository extends JpaRepository<LikedReview, Long> {
    @Query("SELECT lr FROM LikedReview lr WHERE lr.user.username = :username")
    List<LikedReview> findAllByUser_Username(String username);

    boolean existsByUser_UsernameAndReview_ReviewDate(String reviewWriter, LocalDate reviewDate);

    Optional<LikedReview> findByUserAndReview(User user, Review review);
}
