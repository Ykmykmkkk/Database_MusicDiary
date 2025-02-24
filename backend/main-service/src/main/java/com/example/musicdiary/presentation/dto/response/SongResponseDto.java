package com.example.musicdiary.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SongResponseDto {
    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String durationTime;
}
