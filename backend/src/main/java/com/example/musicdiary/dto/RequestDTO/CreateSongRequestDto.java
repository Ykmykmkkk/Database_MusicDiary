package com.example.musicdiary.dto.RequestDTO;

import com.example.musicdiary.domain.Song;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CreateSongRequestDto {

    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String durationTime;
    public Song toEntity() {
        return Song.builder()
                .title(title)
                .artist(artist)
                .album(album)
                .releaseDate(releaseDate)
                .durationTime(durationTime)
                .build();
    }
}
