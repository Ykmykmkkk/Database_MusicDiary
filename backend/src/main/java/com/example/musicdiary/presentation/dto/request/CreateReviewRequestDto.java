package com.example.musicdiary.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReviewRequestDto {

    @NotNull
    private LocalDate reviewDate;
    @NotNull
    private String username;
    @NotNull
    private String songTitle;
    @NotNull
    private String songArtist;
    @NotNull
    private String reviewContent;
    @NotNull
    private Boolean isPublic;

}
