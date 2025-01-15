import 'package:flutter/material.dart';
import 'package:musicdiary/Model/review_model.dart';
import 'package:musicdiary/Service/review_service.dart';
import 'package:musicdiary/Widget/review_card_widget.dart';

class LikedReviewsPage extends StatefulWidget {
  final String username;
  const LikedReviewsPage({super.key, required this.username});

  @override
  State<LikedReviewsPage> createState() => _LikedReviewsPageState();
}

class _LikedReviewsPageState extends State<LikedReviewsPage> {
  late Future<List<ReviewModel>> likedReviews;

  @override
  void initState() {
    super.initState();
    // 좋아요한 노래 데이터를 서버 또는 데이터베이스에서 가져옴
    likedReviews = ReviewService.getLikedReviews(widget.username);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "${widget.username}님의 좋아요한 감상평",
          style: const TextStyle(fontSize: 18),
        ),
      ),
      body: FutureBuilder<List<ReviewModel>>(
        future: likedReviews, // 비동기 데이터
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
                  "좋아요한 감상평이 없습니다.",
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
                        username: review.username,
                        songLiked: review.songLiked,
                        reviewLiked: true,
                        onSongLikePressed: () {
                          setState(() {
                            review.songLiked = !review.songLiked;
                          });
                        },
                        onReviewLikePressed: () {}));
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
