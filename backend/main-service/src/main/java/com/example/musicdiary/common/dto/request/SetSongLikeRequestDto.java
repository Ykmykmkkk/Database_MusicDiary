package com.example.musicdiary.common.dto.request;

import lombok.Data;

@Data
public class SetSongLikeRequestDto {
    private String title;
    private String artist;
}
