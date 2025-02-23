package com.example.musicdiary.common;

import com.example.musicdiary.domain.LikedReviewEntity;
import com.example.musicdiary.domain.LikedSongEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor // ModelMapper에서 필요
public class UserDto {
    private String username;
    private Long id;
    private String password;
    private String name;
    private String email;
    private boolean deleted;
    private List<LikedSongEntity> likedSongEntities;
    private List<LikedReviewEntity> likedReviewEntities;
}
