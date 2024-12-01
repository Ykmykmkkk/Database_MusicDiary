package com.example.musicdiary.service;

import com.example.musicdiary.domain.Review;
import com.example.musicdiary.domain.Song;
import com.example.musicdiary.domain.User;
import com.example.musicdiary.dto.RequestDTO.CreateReviewRequestDto;
import com.example.musicdiary.dto.ResponseDto.ReviewResponseDto;
import com.example.musicdiary.repository.LikedReviewRepository;
import com.example.musicdiary.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LikedReviewRepository likedReviewRepository;
    private final UserService userService;
    private final SongService songService;
@Transactional(isolation = Isolation.SERIALIZABLE)
    public void createReview(CreateReviewRequestDto createReviewRequestDto) {
        String username = createReviewRequestDto.getUsername();
        User user = userService.getUserEntityByUsername(username);
        String songTitle = createReviewRequestDto.getSongTitle();
        String songArtist = createReviewRequestDto.getSongArtist();
        Song song = songService.getSongEntityByTitleAndArtist(songTitle, songArtist);
        Review review = Review.builder()
            .user(user)
            .song(song)
            .reviewContent(createReviewRequestDto.getReviewContent())
            .reviewDate(createReviewRequestDto.getReviewDate())
            .isPublic(createReviewRequestDto.getIsPublic())
            .build();
        reviewRepository.save(review);
    }

    public void createReviewNativeQuery(CreateReviewRequestDto createReviewRequestDto) {
        reviewRepository.createReview(createReviewRequestDto.getIsPublic(),
                createReviewRequestDto.getReviewContent(),
                createReviewRequestDto.getReviewDate(),
                createReviewRequestDto.getSongTitle(),
                createReviewRequestDto.getSongArtist(),
                createReviewRequestDto.getUsername()
        );
    }


    public List<ReviewResponseDto> getAllReview(String username) {
        List<Review> reviews = reviewRepository.findAllByUser_username(username);
        if(reviews.isEmpty()){
            throw new RuntimeException("No review found");
        }
        return toResponseDtoList(reviews);
    }

    public List<ReviewResponseDto> getPublicReview() {
        List<Review> reviews = reviewRepository.findAllByIsPublicTrue();
        if(reviews.isEmpty()){
            throw new RuntimeException("No public review found");
        }
        return toResponseDtoList(reviews);
    }

    public ReviewResponseDto getReviewDate(String username, LocalDate date) {
        Review review = reviewRepository.findByUser_usernameAndReviewDate(username, date).
                orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
        return ReviewResponseDto.builder()
               .id(review.getId())
               .username(review.getUser().getUsername())
               .title(review.getSong().getTitle())
               .artist(review.getSong().getArtist())
               .reviewContent(review.getReviewContent())
               .reviewDate(review.getReviewDate().toString())
               .isPublic(review.getIsPublic())
               .build();
    }

    public List<ReviewResponseDto> toResponseDtoList(List<Review> reviews) {
        return reviews.stream()
               .map(review -> ReviewResponseDto.builder()
                       .id(review.getId())
                       .username(review.getUser().getUsername())
                       .title(review.getSong().getTitle())
                       .artist(review.getSong().getArtist())
                       .reviewContent(review.getReviewContent())
                       .reviewDate(review.getReviewDate().toString())
                       .isPublic(review.getIsPublic())
                       .build())
               .toList();
    }

    public Review getReviewEntityById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));
    }

}
