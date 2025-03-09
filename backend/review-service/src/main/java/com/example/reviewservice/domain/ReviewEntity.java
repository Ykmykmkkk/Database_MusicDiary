package com.example.reviewservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "review_date"})
})
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate reviewDate;
    private UUID writerId;
    private String writerUsername;

    private Long songId;
    private String songTitle;  // 노래 제목
    private String songArtist; // 아티스트

    private String reviewTitle;
    private String reviewContent;
    private Boolean isPublic;
}
