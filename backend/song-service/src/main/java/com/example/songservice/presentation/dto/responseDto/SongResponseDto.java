package com.example.songservice.presentation.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SongResponseDto {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private String durationTime;
    private Boolean isLike;
}
