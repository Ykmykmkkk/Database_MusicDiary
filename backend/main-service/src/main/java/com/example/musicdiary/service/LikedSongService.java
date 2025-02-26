package com.example.musicdiary.service;

import com.example.musicdiary.common.SongDto;

import com.example.musicdiary.domain.LikedSongEntity;
import com.example.musicdiary.domain.SongEntity;
import com.example.musicdiary.domain.UserEntity;
import com.example.musicdiary.repository.LikedSongRepository;
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void setSongLike(String userId, String songId) {

        boolean isExist = likedSongRepository.existsByUserEntity_IdAndSongEntity_Id(userId, songId);
        if(isExist){
            throw new IllegalArgumentException("Already liked this song");
        }
        UserEntity userEntity = userService.getUserEntityByUsername(userId);
        String title = setSongLikeDto.getTitle();
        String artist = setSongLikeDto.getArtist();
        SongEntity songEntity = songService.getSongEntityByTitleAndArtist(title, artist);
        LikedSongEntity likedSongEntity = LikedSongEntity.builder()
                .userEntity(userEntity)
                .songEntity(songEntity)
                .build();
        likedSongRepository.save(likedSongEntity);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void setSongUnlike(String userId, SongDto setSongLikeDto) {
        UserEntity userEntity = userService.getUserEntityByUsername(userId);
        String title = setSongLikeDto.getTitle();
        String artist = setSongLikeDto.getArtist();
        SongEntity song = songService.getSongEntityByTitleAndArtist(title, artist);
        LikedSongEntity likedSongEntity = likedSongRepository.findByUserEntityAndSongEntity(userEntity, song)
                .orElseThrow(() -> new EntityNotFoundException("LikedSongEntity not found"));
        likedSongRepository.delete(likedSongEntity);
    }
    @Transactional(readOnly = true)
    public List<SongDto> getLikedSongListByUsername(String userId) {
        return toSongDtoList(
                likedSongRepository.findAllByUser_Username(userId).stream()
                .map(LikedSongEntity::getSongEntity)
                .toList()
        );
    }

    public List<SongDto> toSongDtoList(List<SongEntity> songs) {
        return songs.stream()
                .map(song -> SongDto.builder()
                        .title(song.getTitle())
                        .artist(song.getArtist())
                        .releaseDate(song.getReleaseDate())
                        .album(song.getAlbum())
                        .durationTime(song.getDurationTime())
                        .build())
                .toList();
    }


}
