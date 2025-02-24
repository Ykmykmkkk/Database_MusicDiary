package com.example.musicdiary.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor // ModelMapper가 필요로 하는 기본생성자가 클래스에 있어야 함.
@AllArgsConstructor
public class ReviewResponseDto {
    private LocalDate reviewDate;
    private String reviewContent;
    private String username;
    private String title;
    private String artist;
    private Boolean isPublic;
    private Boolean isLike;
}
