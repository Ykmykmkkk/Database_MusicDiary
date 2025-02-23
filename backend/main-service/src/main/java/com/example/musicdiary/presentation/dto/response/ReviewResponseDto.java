package com.example.musicdiary.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ReviewResponseDto {

    private LocalDate reviewDate;

    private String reviewContent;

    private String username;

    private String title;

    private String artist;

    private Boolean isPublic;
}
