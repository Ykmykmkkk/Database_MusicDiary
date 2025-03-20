package com.example.reviewservice.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@NoArgsConstructor
public class SongResponseDto {
    private String title;
    private String artist;

    @Builder
    public SongResponseDto(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }
}
