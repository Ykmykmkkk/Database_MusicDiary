package com.example.musicdiary.dto.RequestDTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReviewRequestDto {

    private LocalDate reviewDate;
    private String username;
    private String songTitle;
    private String songArtist;
    private String reviewContent;
    private Boolean isPublic;

}
