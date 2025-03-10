package com.example.songservice.repository;

import com.example.songservice.domain.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository <SongEntity, Long> {
    Optional<SongEntity> findByTitleAndArtist(String title, String artist);
    Optional<SongEntity> findById(long id);


    boolean existsByTitleAndArtist(String title, String artist);
}
