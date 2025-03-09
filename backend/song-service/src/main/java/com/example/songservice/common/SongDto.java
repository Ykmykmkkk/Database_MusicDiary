package com.example.songservice.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor // ModelMapper에서 필요
public class SongDto {
    private Long id;            // 고유 식별자
    private String title;       // 노래 제목
    private String artist;      // 아티스트
    private String album;       // 앨범
    private LocalDate releaseDate;  // 출시 날짜
    private String durationTime;    // 재생 시간
    private Boolean isLike;
}
