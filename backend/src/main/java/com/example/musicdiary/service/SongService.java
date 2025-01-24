package com.example.musicdiary.service;

import com.example.musicdiary.entity.Song;
import com.example.musicdiary.dto.RequestDTO.CreateSongRequestDto;
import com.example.musicdiary.dto.ResponseDto.SongResponseDto;
import com.example.musicdiary.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createSong(CreateSongRequestDto createSongRequestDto) {
        boolean isExists = songRepository.existsByTitleAndArtist(
                createSongRequestDto.getTitle()
                ,createSongRequestDto.getArtist());
        if(isExists){
            throw new IllegalArgumentException("이미 존재하는 노래입니다");
        }
        else{
            songRepository.save(createSongRequestDto.toEntity());
        }
    }
    @Transactional(readOnly = true)
    public SongResponseDto getSongByTitleAndArtist(String title, String artist) {
        Song song = songRepository.findByTitleAndArtist(title, artist)
                .orElseThrow(() -> new IllegalArgumentException("노래가 존재하지 않습니다"));
        return SongResponseDto.builder()
               .title(song.getTitle())
               .artist(song.getArtist())
               .album(song.getAlbum())
               .releaseDate(song.getReleaseDate())
               .durationTime(song.getDurationTime())
               .build();
    }
    public Song getSongEntityByTitleAndArtist(String title, String artist) {
        return songRepository.findByTitleAndArtist(title, artist).orElseThrow(() -> new IllegalArgumentException("노래가 존재하지 않습니다"));
    }
}
