package com.example.musicdiary.dto;

import com.example.musicdiary.domain.LikedReview;
import com.example.musicdiary.domain.LikedSong;
import com.example.musicdiary.domain.User;
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
    private List<LikedSong> likedSongs = new ArrayList<>();
    @Builder.Default
    private List<LikedReview> likedReviews = new ArrayList<>();
    public User toEntity() {
        return User.builder()
                .id(id)
                .username(username)
                .name(name)
                .password(password)
                .email(email)
                .likedSongs(likedSongs)
                .likedReviews(likedReviews)
                .build();
        }


}
