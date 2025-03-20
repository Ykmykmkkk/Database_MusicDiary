package com.example.reviewservice.presentation.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateReviewRequestDto {

    @NotNull
    private LocalDate reviewDate;
    @NotNull
    private UUID writerId;

    @NotNull
    private Long songId;

    @NotNull
    private String reviewTitle;
    @NotNull
    private String reviewContent;
    @NotNull
    private Boolean isPublic;

}
