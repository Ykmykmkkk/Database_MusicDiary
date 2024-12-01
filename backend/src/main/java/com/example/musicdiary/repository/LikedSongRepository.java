package com.example.musicdiary.repository;

import com.example.musicdiary.domain.LikedReview;
import com.example.musicdiary.domain.LikedSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {
    @Query("SELECT sg.song FROM LikedSong sg WHERE sg.user.username = :username")
    List<LikedSong> findAllByUser_Username(String username);
}
