package com.example.musicdiary.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class SongResponseDto {
    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String durationTime;
}
