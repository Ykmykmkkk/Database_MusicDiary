package com.example.musicdiary.repository;

import com.example.musicdiary.entity.LikedSongEntity;
import com.example.musicdiary.entity.SongEntity;
import com.example.musicdiary.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikedSongRepository extends JpaRepository<LikedSongEntity, Long> {
    @Query("SELECT sg FROM LikedSongEntity sg WHERE sg.userEntity.username = :username")
    List<LikedSongEntity> findAllByUser_Username(String username);

    boolean existsByUserEntity_UsernameAndSongEntity_TitleAndSongEntity_Artist(String username, String title, String artist);
    Optional<LikedSongEntity> findByUserEntityAndSongEntity(UserEntity userEntity, SongEntity song);
}
