package com.example.musicdiary.service;

import com.example.musicdiary.domain.*;
import com.example.musicdiary.dto.RequestDTO.SetReviewLikeRequestDto;
import com.example.musicdiary.dto.RequestDTO.SetSongLikeRequestDto;
import com.example.musicdiary.repository.LikedReviewRepository;
import com.example.musicdiary.repository.LikedSongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikedSongService {
    private final LikedSongRepository likedSongRepository;
    private final UserService userService;
    private final SongService songService;
    public void setSongLike(String username, SetSongLikeRequestDto setSongLikeRequestDto) {
        User user = userService.getUserEntityByUsername(username);
        String title = setSongLikeRequestDto.getTitle();
        String artist = setSongLikeRequestDto.getArtist();
        Song song = songService.getSongEntityByTitleAndArtist(title, artist);
        LikedSong likedSong = LikedSong.builder()
                .user(user)
                .song(song)
                .build();
        likedSongRepository.save(likedSong);
    }
    public List<Song> getLikedSongListByUsername(String username) {
        return likedSongRepository.findAllByUser_Username(username).stream()
                .map(LikedSong::getSong)
                .toList();
    }
}
