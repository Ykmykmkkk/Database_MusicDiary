package com.example.songservice.application;

import com.example.songservice.common.SongDto;
import com.example.songservice.domain.LikedSongEntity;
import com.example.songservice.domain.SongEntity;
import com.example.songservice.repository.LikedSongRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LikedSongService {
    private final LikedSongRepository likedSongRepository;
    private final SongService songService;
    // private final RedisTemplate<String, Object> redisTemplate;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void likeSong(UUID userId, Long songId) {
        // redisTemplate.opsForSet().add("user:" + userId + ":liked_songs", songId.toString());

        SongEntity songEntity =
                songService.getSongEntityById(songId);

        LikedSongEntity likedSongEntity = LikedSongEntity.builder()
                .userId(userId)
                .songEntity(songEntity)
                .build();

        likedSongRepository.save(likedSongEntity);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void unlikeSong(UUID userId, Long songId) {
        LikedSongEntity likedSongEntity = likedSongRepository.findByUserIdAndSongEntityId(userId, songId)
                .orElseThrow(() -> new EntityNotFoundException("LikedSongEntity not found"));
        likedSongRepository.delete(likedSongEntity);
    }
    // 이거랑 아래 메소드 구현
    @Transactional(readOnly = true)
    public List<SongDto> getLikedSongListByUserId(UUID userId) {
        return toSongDtoList(
                likedSongRepository.findAllByUserId(userId).stream()
                        .map(LikedSongEntity::getSongEntity)
                        .toList(), true
        );
    }

    public List<SongDto> toSongDtoList(List<SongEntity> songs, Boolean isLike) {
        return songs.stream()
                .map(song -> SongDto.builder()
                        .title(song.getTitle())
                        .artist(song.getArtist())
                        .releaseDate(song.getReleaseDate())
                        .album(song.getAlbum())
                        .durationTime(song.getDurationTime())
                        .isLike(isLike)
                        .build())
                .toList();
    }

    public Boolean isLikedSong(UUID userId, Long songId) {
        return likedSongRepository.existsByUserIdAndSongEntityId(userId,songId);
    }
}
