package com.example.reviewservice.common;



import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "writerUsername"})
public class ReviewDto {
    private Long id;
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
