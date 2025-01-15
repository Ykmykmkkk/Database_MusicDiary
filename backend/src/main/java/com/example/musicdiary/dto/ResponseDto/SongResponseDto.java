package com.example.musicdiary.dto.ResponseDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class SongResponseDto {
    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String durationTime;
}
