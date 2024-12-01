package com.example.musicdiary.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "songs", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "review_date"}))
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private long id;

    @Column(unique = true, nullable = false)
    private String title;
    @Column(nullable = false)
    private String artist;
    @Column(nullable = false)
    private String album;
    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private String durationTime;



}
