package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedSongEntity;
import com.example.musicdiary.domain.SongEntity;
import com.example.musicdiary.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikedSongRepository extends JpaRepository<LikedSongEntity, Long> {

    List<LikedSongEntity> findAllByUserEntityId(Long userId);

    boolean existsByUserEntityIdAndSongEntityTitleAndSongEntityArtist(Long userId, String title, String artist);
    Optional<LikedSongEntity> findByUserEntityAndSongEntity(UserEntity userEntity, SongEntity song);
}
