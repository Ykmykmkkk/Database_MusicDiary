package com.example.reviewservice.presentation.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private LocalDate reviewDate;
    private UUID writerId;
    private String writerUsername;
    private Long songId;
    private String songTitle;
    private String songArtist;
    private Boolean songLiked;
    private String reviewTitle;
    private String reviewContent;
    private Boolean isPublic;
    private Boolean reviewLiked;
}
