package com.example.musicdiary.repository;

import com.example.musicdiary.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByIsPublicTrue();
    List<Review> findAllByUser_username(String username);

    Optional<Review> findByUser_usernameAndReviewDate(String username, LocalDate date);
    @Modifying
    @Query(value = """
    INSERT INTO review (is_public, review_content, review_date, song_id, user_id)
    SELECT :isPublic, :reviewContent, :reviewDate, 
           (SELECT id FROM song WHERE title = :songTitle AND artist = :songArtist),
           (SELECT id FROM user WHERE username = :username)
    WHERE NOT EXISTS (
        SELECT 1 
        FROM review r
        JOIN user u ON r.user_id = u.id
        WHERE u.username = :username AND r.review_date = :reviewDate
    )
""", nativeQuery = true)
    void createReview(
            @Param("isPublic") boolean isPublic,
            @Param("reviewContent") String reviewContent,
            @Param("reviewDate") LocalDate reviewDate,
            @Param("songTitle") String songTitle,
            @Param("songArtist") String songArtist,
            @Param("username") String username
    );


}
