package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedReview;
import com.example.musicdiary.domain.LikedSong;
import com.example.musicdiary.domain.Song;
import com.example.musicdiary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {
    @Query("SELECT sg FROM LikedSong sg WHERE sg.user.username = :username")
    List<LikedSong> findAllByUser_Username(String username);

    boolean existsByUser_UsernameAndSong_TitleAndSong_Artist(String username, String title, String artist);
    Optional<LikedSong> findByUserAndSong(User user, Song song);
}
