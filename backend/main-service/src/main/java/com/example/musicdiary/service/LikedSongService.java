package com.example.musicdiary.service;

import com.example.musicdiary.domain.*;
import com.example.musicdiary.common.dto.request.SetSongLikeRequestDto;
import com.example.musicdiary.common.dto.response.SongResponseDto;
import com.example.musicdiary.repository.LikedSongRepository;
import com.example.musicdiary.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikedSongService {
    private final LikedSongRepository likedSongRepository;
    private final UserService userService;
    private final SongService songService;
    private final UserRepository userRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void setSongLike(String username, SetSongLikeRequestDto setSongLikeRequestDto) {

        boolean isExist = likedSongRepository.existsByUserEntity_UsernameAndSongEntity_TitleAndSongEntity_Artist(username, setSongLikeRequestDto.getTitle(), setSongLikeRequestDto.getArtist());
        if(isExist){
            throw new IllegalArgumentException("Already liked this song");
        }
        UserEntity userEntity = userService.getUserEntityByUsername(username);
        String title = setSongLikeRequestDto.getTitle();
        String artist = setSongLikeRequestDto.getArtist();
        SongEntity songEntity = songService.getSongEntityByTitleAndArtist(title, artist);
        LikedSongEntity likedSongEntity = LikedSongEntity.builder()
                .userEntity(userEntity)
                .songEntity(songEntity)
                .build();
        likedSongRepository.save(likedSongEntity);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void setSongUnlike(String username, SetSongLikeRequestDto setSongLikeRequestDto) {
        UserEntity userEntity = userService.getUserEntityByUsername(username);
        String title = setSongLikeRequestDto.getTitle();
        String artist = setSongLikeRequestDto.getArtist();
        SongEntity song = songService.getSongEntityByTitleAndArtist(title, artist);
        LikedSongEntity likedSongEntity = likedSongRepository.findByUserEntityAndSongEntity(userEntity, song)
                .orElseThrow(() -> new EntityNotFoundException("LikedSongEntity not found"));
        likedSongRepository.delete(likedSongEntity);
    }
    @Transactional(readOnly = true)
    public List<SongResponseDto> getLikedSongListByUsername(String username) {
        List<SongEntity> likedSongs = likedSongRepository.findAllByUser_Username(username).stream()
                .map(LikedSongEntity::getSongEntity)
                .toList();
        return toResponseDtoList(likedSongs);
    }

    public List<SongResponseDto> toResponseDtoList(List<SongEntity> songs) {
        return songs.stream()
                .map(song -> SongResponseDto.builder()
                        .title(song.getTitle())
                        .artist(song.getArtist())
                        .releaseDate(song.getReleaseDate())
                        .album(song.getAlbum())
                        .durationTime(song.getDurationTime())
                        .build())
                .toList();
    }


}
