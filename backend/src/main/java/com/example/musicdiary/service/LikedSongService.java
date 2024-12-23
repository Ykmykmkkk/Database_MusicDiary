package com.example.musicdiary.service;

import com.example.musicdiary.domain.*;
import com.example.musicdiary.dto.RequestDTO.SetReviewLikeRequestDto;
import com.example.musicdiary.dto.RequestDTO.SetSongLikeRequestDto;
import com.example.musicdiary.dto.ResponseDto.ReviewResponseDto;
import com.example.musicdiary.dto.ResponseDto.SongResponseDto;
import com.example.musicdiary.repository.LikedReviewRepository;
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

        boolean isExist = likedSongRepository.existsByUser_UsernameAndSong_TitleAndSong_Artist(username, setSongLikeRequestDto.getTitle(), setSongLikeRequestDto.getArtist());
        if(isExist){
            throw new IllegalArgumentException("Already liked this song");
        }
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void setSongUnlike(String username, SetSongLikeRequestDto setSongLikeRequestDto) {
        User user = userService.getUserEntityByUsername(username);
        String title = setSongLikeRequestDto.getTitle();
        String artist = setSongLikeRequestDto.getArtist();
        Song song = songService.getSongEntityByTitleAndArtist(title, artist);
        LikedSong likedSong = likedSongRepository.findByUserAndSong(user, song)
                .orElseThrow(() -> new EntityNotFoundException("LikedSong not found"));
        likedSongRepository.delete(likedSong);
    }
    @Transactional(readOnly = true)
    public List<SongResponseDto> getLikedSongListByUsername(String username) {
        List<Song> likedSongs = likedSongRepository.findAllByUser_Username(username).stream()
                .map(LikedSong::getSong)
                .toList();
        return toResponseDtoList(likedSongs);
    }

    public List<SongResponseDto> toResponseDtoList(List<Song> songs) {
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
