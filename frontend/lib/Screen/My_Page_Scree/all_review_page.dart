import 'package:flutter/material.dart';
import 'package:musicdiary/Model/review_model.dart';
import 'package:musicdiary/Service/review_service.dart';
import 'package:musicdiary/Service/song_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';
import 'package:musicdiary/Widget/review_card_widget.dart';

class AllReviewPage extends StatefulWidget {
  final String userId;
  final String username;
  const AllReviewPage(
      {super.key, required this.userId, required this.username});

  @override
  State<AllReviewPage> createState() => _AllReviewPageState();
}

class _AllReviewPageState extends State<AllReviewPage> {
  late Future<List<ReviewModel>> allReviews;

  @override
  void initState() {
    super.initState();
    // 좋아요한 노래 데이터를 서버 또는 데이터베이스에서 가져옴
    allReviews = ReviewService.getAllReviews(widget.userId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "${widget.username}님의 작성한 감상평",
          style: const TextStyle(fontSize: 18),
        ),
      ),
      body: FutureBuilder<List<ReviewModel>>(
        future: allReviews, // 비동기 데이터
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            // 데이터 로딩 중
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            // 에러 처리
            return Center(
              child: Text(
                "오류가 발생했습니다: ${snapshot.error}",
                style: const TextStyle(color: Colors.red, fontSize: 16),
                textAlign: TextAlign.center,
              ),
            );
          } else if (snapshot.hasData) {
            final reviews = snapshot.data!;
            if (reviews.isEmpty) {
              // 좋아요한 노래가 없는 경우
              return const Center(
                child: Text(
                  "작성한 감상평이 없습니다.",
                  style: TextStyle(fontSize: 18, color: Colors.grey),
                ),
              );
            }

            // 데이터가 있는 경우 ListView.builder로 렌더링
            return ListView.builder(
              padding: const EdgeInsets.all(16.0),
              itemCount: reviews.length,
              itemBuilder: (context, index) {
                final review = reviews[index];
                return Padding(
                    padding: const EdgeInsets.only(bottom: 16.0),
                    child: ReviewCardWidget(
                      reviewData: review,
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
                    ));
              },
            );
          } else {
            // snapshot.hasData가 false인 경우
            return const Center(
              child: Text(
                "데이터를 불러오지 못했습니다.",
                style: TextStyle(fontSize: 18, color: Colors.grey),
              ),
            );
          }
        },
      ),
    );
  }
}
