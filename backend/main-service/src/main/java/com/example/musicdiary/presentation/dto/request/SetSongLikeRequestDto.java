package com.example.musicdiary.presentation.dto.request;

import lombok.Data;

@Data
public class SetSongLikeRequestDto {
    private String title;
    private String artist;
}
