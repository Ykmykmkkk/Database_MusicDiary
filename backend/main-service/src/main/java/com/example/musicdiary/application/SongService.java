package com.example.musicdiary.application;

import com.example.musicdiary.common.SongDto;
import com.example.musicdiary.domain.SongEntity;
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
    public void createSong(SongDto createSongDto) {
        boolean isExists = songRepository.existsByTitleAndArtist(
                createSongDto.getTitle()
                ,createSongDto.getArtist());
        if(isExists){
            throw new IllegalArgumentException("이미 존재하는 노래입니다");
        }
        else{
            SongEntity songEntity = SongEntity.builder()
                    .artist(createSongDto.getArtist())
                    .album(createSongDto.getAlbum())
                    .title(createSongDto.getTitle())
                    .releaseDate(createSongDto.getReleaseDate())
                    .durationTime(createSongDto.getDurationTime()).build();
            songRepository.save(songEntity);
        }
    }
    @Transactional(readOnly = true)
    public SongDto getSongByTitleAndArtist(String title, String artist) {
        SongEntity songEntity = songRepository.findByTitleAndArtist(title, artist)
                .orElseThrow(() -> new IllegalArgumentException("노래가 존재하지 않습니다"));
        return SongDto.builder()
               .title(songEntity.getTitle())
               .artist(songEntity.getArtist())
               .album(songEntity.getAlbum())
               .releaseDate(songEntity.getReleaseDate())
               .durationTime(songEntity.getDurationTime())
               .build();
    }
    public SongEntity getSongEntityByTitleAndArtist(String title, String artist) {
        return songRepository.findByTitleAndArtist(title, artist).orElseThrow(() -> new IllegalArgumentException("노래가 존재하지 않습니다"));
    }
}
