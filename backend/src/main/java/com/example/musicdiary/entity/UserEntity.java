package com.example.musicdiary.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    long id;

    @Column(unique = true, nullable = false)
    @Size(min=1, max=10)
    private String username;

    @Column(nullable = false)
    @Size(min=1, max=10) // 서비스 레이어에서 도메인 객체에 대한 유효성 검사를 진행할 때 쓰임
    private String password;

    @Column(nullable = false) // 데이터베이스 테이블에서 not null 옵션 설정
    @Size(min=1, max=10) // 서비스 레이어에서 도메인 객체에 대한 유효성 검사를 진행할 때 쓰임
    private String name;

    @Column(nullable = false)
    @NotNull
    private String email;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Builder.Default
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.PERSIST)
    private List<LikedSongEntity> likedSongEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.PERSIST)
    private List<LikedReviewEntity> likedReviewEntities = new ArrayList<>();

    public void delete() {
        this.deleted = true;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
