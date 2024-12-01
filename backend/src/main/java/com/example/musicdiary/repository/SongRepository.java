package com.example.musicdiary.repository;

import com.example.musicdiary.domain.Song;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByTitleAndArtist(String title, String artist);
    Optional<Song> findByTitleAndArtist(String title, String artist);
}
