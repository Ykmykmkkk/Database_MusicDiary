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
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.song WHERE r.isPublic = true")
    List<Review> findAllByIsPublicTrue();
    List<Review> findAllByUser_username(String username);

    Optional<Review> findByUser_usernameAndReviewDate(String username, LocalDate date);

    boolean existsByUser_usernameAndReviewDate(String username, LocalDate reviewDate);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.username = :username AND r.reviewDate = :reviewDate")
    Long countByUsernameAndReviewDate(String username, LocalDate reviewDate);

    @Modifying
    @Query(value = """
    INSERT INTO review (is_public, review_content, review_date, song_id, user_id)
    SELECT :isPublic, :reviewContent, :reviewDate, 
           (SELECT song_id FROM song WHERE title = :songTitle AND artist = :songArtist),
           (SELECT user_id FROM user WHERE username = :username)
    WHERE NOT EXISTS (
        SELECT 1 FROM review r
        JOIN user u ON r.user_id = u.user_id
        WHERE u.username = :username AND r.review_date = :reviewDate
    )
    """, nativeQuery = true)
    void createReviewbyNativequery(
            @Param("isPublic") boolean isPublic,
            @Param("reviewContent") String reviewContent,
            @Param("reviewDate") LocalDate reviewDate,
            @Param("songTitle") String songTitle,
            @Param("songArtist") String songArtist,
            @Param("username") String username
    );


}
