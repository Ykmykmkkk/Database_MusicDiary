package com.example.musicdiary.presentation.dto.request;

import com.example.musicdiary.entity.SongEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSongRequestDto {

    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String durationTime;
    public SongEntity toEntity() {
        return SongEntity.builder()
                .title(title)
                .artist(artist)
                .album(album)
                .releaseDate(releaseDate)
                .durationTime(durationTime)
                .build();
    }
}
