package com.example.musicdiary.common;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor // ModelMapper에서 필요
public class ReviewDto {
    private Long id;              // 리뷰 ID
    private LocalDate reviewDate; // 리뷰 작성 날짜

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewDto reviewDto)) return false;
        return Objects.equals(getId(), reviewDto.getId()) && Objects.equals(getUsername(), reviewDto.getUsername());
    }

    // UserEntity 대신 필요한 필드만
    private Long userId;          // 사용자 ID
    private String username;      // 사용자 이름

    // SongEntity 대신 필요한 필드만
    private Long songId;          // 노래 ID
    private String songTitle;     // 노래 제목
    private String songArtist;    // 노래 아티스트

    private String reviewContent; // 리뷰 내용
    private Boolean isPublic;     // 공개 여부

    private Boolean isLike; // 좋아요를 눌렀는지
}
