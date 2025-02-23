package com.example.musicdiary.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "songs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "artist"})
})
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private long id;

    @Size(max = 30) // DB와 일치
    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Size(max = 30)
    @Column(name = "artist", length = 30)
    private String artist;

    @Column(nullable = false)
    private String album;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private String durationTime;
}