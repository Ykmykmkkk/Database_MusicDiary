package com.example.musicdiary.repository;

import com.example.musicdiary.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("SELECT r FROM ReviewEntity r JOIN FETCH r.userEntity JOIN FETCH r.songEntity WHERE r.isPublic = true")
    List<ReviewEntity> findAllByIsPublicTrue();
    List<ReviewEntity> findAllByUserEntityId(Long userId);

    Optional<ReviewEntity> findByUserEntityIdAndReviewDate(Long userId, LocalDate date);

    boolean existsByUserEntity_usernameAndReviewDate(String username, LocalDate reviewDate);

    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.userEntity.username = :username AND r.reviewDate = :reviewDate")
    Long countByUsernameAndReviewDate(String username, LocalDate reviewDate);

    @Modifying
    @Query(value = """
    INSERT INTO reviewEntity (is_public, review_content, review_date, song_id, user_id)
    SELECT :isPublic, :reviewContent, :reviewDate, 
           (SELECT song_id FROM songs WHERE title = :songTitle AND artist = :songArtist),
           (SELECT user_id FROM users WHERE username = :username)
    WHERE NOT EXISTS (
        SELECT 1 FROM reviewEntity r
        JOIN userEntity u ON r.user_id = u.user_id
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


    Optional<ReviewEntity> findByUserEntity_UsernameAndReviewDate(String username, LocalDate date);
}
