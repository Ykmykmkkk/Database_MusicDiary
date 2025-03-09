package com.example.songservice.presentation.dto.requestDto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSongRequestDto {
    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String durationTime;
}
