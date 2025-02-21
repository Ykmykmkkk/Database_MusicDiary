package com.example.musicdiary.repository;

import com.example.musicdiary.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, Long> {
    boolean existsByTitleAndArtist(String title, String artist);
    Optional<SongEntity> findByTitleAndArtist(String title, String artist);
}
