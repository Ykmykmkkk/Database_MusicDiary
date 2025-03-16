import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:musicdiary/Model/review_model.dart';
import 'package:musicdiary/Service/song_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';
import 'package:musicdiary/Widget/review_card_widget.dart';
import 'package:musicdiary/Service/review_service.dart';

class ReviewPage extends StatefulWidget {
  final String username;

  const ReviewPage({super.key, required this.username});

  @override
  State<ReviewPage> createState() => _ReviewPageState();
}

class _ReviewPageState extends State<ReviewPage> {
  late Future<List<ReviewModel>> publicReviews;

  @override
  void initState() {
    super.initState();
    publicReviews = ReviewService.getPublicReviews();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("감상평 게시판"),
      ),
      body: FutureBuilder<List<ReviewModel>>(
        future: publicReviews,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(
              child: Text(
                "오류가 발생했습니다: ${snapshot.error}",
                style: const TextStyle(color: Colors.red, fontSize: 16),
              ),
            );
          } else if (snapshot.hasData) {
            final reviews = snapshot.data!;
            if (reviews.isEmpty) {
              return const Center(
                child: Text(
                  "공개된 감상평이 없습니다.",
                  style: TextStyle(fontSize: 18, color: Colors.grey),
                ),
              );
            }

            return ListView.builder(
              padding: const EdgeInsets.all(16.0),
              itemCount: reviews.length,
              itemBuilder: (context, index) {
                final review = reviews[index];
                return Padding(
                  padding: const EdgeInsets.only(bottom: 16.0),
                  child: ReviewCardWidget(
                    reviewData: review,
                    username: review.writerUsername,
                    songLiked: review.songLiked,
                    reviewLiked: review.reviewLiked,
                    onSongLikePressed: () async {
                      try {
                        if (review.songLiked) {
                          await SongService.unlikeSong(review.songId);
                        } else {
                          await SongService.likeSong(review.songId);
                        }
                        setState(() {
                          review.songLiked = !review.songLiked;
                        });
                      } catch (e) {
                        showErrorDialog(context, "좋아요 처리 중 오류 발생");
                      }
                    },
                    onReviewLikePressed: () async {
                      try {
                        if (review.reviewLiked) {
                          await ReviewService.unlikeReview(review.reviewId);
                        } else {
                          await ReviewService.likeReview(review.reviewId);
                        }
                        setState(() {
                          review.reviewLiked = !review.reviewLiked;
                        });
                      } catch (e) {
                        showErrorDialog(context, "좋아요 처리 중 오류 발생");
                      }
                    },
                  ),
                );
              },
            );
          }
          return const Center(
            child: Text(
              "데이터를 불러오지 못했습니다.",
              style: TextStyle(fontSize: 18, color: Colors.grey),
            ),
          );
        },
      ),
    );
  }
}
