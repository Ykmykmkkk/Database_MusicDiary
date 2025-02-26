package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedSongEntity;
import com.example.musicdiary.domain.SongEntity;
import com.example.musicdiary.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikedSongRepository extends JpaRepository<LikedSongEntity, Long> {
    @Query("SELECT sg FROM LikedSongEntity sg WHERE sg.userEntity.username = :username")
    List<LikedSongEntity> findAllByUser_Username(String username);

    boolean existsByUserEntity_IdAndSongEntity_Id(String userId, String songId);
    Optional<LikedSongEntity> findByUserEntityAndSongEntity(UserEntity userEntity, SongEntity song);
}
