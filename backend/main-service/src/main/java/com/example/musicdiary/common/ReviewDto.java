package com.example.musicdiary.common;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReviewDto {
    private Long id;              // 리뷰 ID
    private LocalDate reviewDate; // 리뷰 작성 날짜

    // UserEntity 대신 필요한 필드만
    private Long userId;          // 사용자 ID
    private String username;      // 사용자 이름

    // SongEntity 대신 필요한 필드만
    private Long songId;          // 노래 ID
    private String songTitle;     // 노래 제목
    private String songArtist;    // 노래 아티스트

    private String reviewContent; // 리뷰 내용
    private Boolean isPublic;     // 공개 여부
}
