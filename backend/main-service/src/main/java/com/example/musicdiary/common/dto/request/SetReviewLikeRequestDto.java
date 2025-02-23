package com.example.musicdiary.common.dto.request;


import lombok.Data;

import java.time.LocalDate;

@Data
public class SetReviewLikeRequestDto {
     private LocalDate reviewDate;
     private String reviewWriter;
}
