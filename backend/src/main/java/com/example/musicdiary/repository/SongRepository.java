package com.example.musicdiary.repository;

import com.example.musicdiary.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByTitleAndArtist(String title, String artist);
    Optional<Song> findByTitleAndArtist(String title, String artist);
}
