package com.example.songservice.repository;

import com.example.songservice.domain.LikedSongEntity;
import com.example.songservice.domain.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikedSongRepository extends JpaRepository<LikedSongEntity,Long> {
    boolean existsByUserIdAndSongEntityId(UUID userId, Long songId);

    Optional<LikedSongEntity> findByUserIdAndSongEntityId(UUID userId, Long song);

    List<LikedSongEntity> findAllByUserId(UUID userId);
}
