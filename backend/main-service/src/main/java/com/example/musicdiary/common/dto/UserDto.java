package com.example.musicdiary.common.dto;

import com.example.musicdiary.domain.LikedReviewEntity;
import com.example.musicdiary.domain.LikedSongEntity;
import com.example.musicdiary.domain.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserDto {
    private long id;
    private String username;
    private String name;
    private String password;
    private String email;
    @Builder.Default
    private List<LikedSongEntity> likedSongEntities = new ArrayList<>();
    @Builder.Default
    private List<LikedReviewEntity> likedReviewEntities = new ArrayList<>();
    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .name(name)
                .password(password)
                .email(email)
                .likedSongEntities(likedSongEntities)
                .likedReviewEntities(likedReviewEntities)
                .build();
        }
}
