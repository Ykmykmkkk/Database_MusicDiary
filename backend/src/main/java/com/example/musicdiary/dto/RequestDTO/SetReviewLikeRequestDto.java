package com.example.musicdiary.dto.RequestDTO;


import lombok.Data;

import java.time.LocalDate;

@Data
public class SetReviewLikeRequestDto {
     private LocalDate reviewDate;
     private String reviewWriter;
}
