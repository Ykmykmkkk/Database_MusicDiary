package com.example.musicdiary.dto.ResponseDto;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class ReviewResponseDto {

    private long id;

    private String reviewDate;

    private String reviewContent;

    private String username;

    private String title;

    private String artist;

    private Boolean isPublic;
}
