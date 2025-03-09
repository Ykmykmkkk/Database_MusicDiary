package com.example.songservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "liked_songs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "song_id"})
})
public class LikedSongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private SongEntity songEntity;  // 다대일 관계 (Many-to-One)
}
