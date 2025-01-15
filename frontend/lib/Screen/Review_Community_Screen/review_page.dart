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
    // 비동기 데이터 로드
    publicReviews = ReviewService.getPublicReviews(widget.username);
  }

  @override
  Widget build(BuildContext context) {
    bool songLiked = false;
    return Scaffold(
      appBar: AppBar(
        title: const Text("감상평 게시판"),
      ),
      body: FutureBuilder<List<ReviewModel>>(
        future: publicReviews, // 비동기 데이터
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            // 로딩 상태
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            // 에러 상태x
            return Center(
              child: Text(
                "오류가 발생했습니다: ${snapshot.error}",
                style: const TextStyle(color: Colors.red, fontSize: 16),
              ),
            );
          } else if (snapshot.hasData) {
            final reviews = snapshot.data!;
            if (reviews.isEmpty) {
              // 데이터가 없는 경우
              return const Center(
                child: Text(
                  "공개된 감상평이 없습니다.",
                  style: TextStyle(fontSize: 18, color: Colors.grey),
                ),
              );
            }

            // 데이터가 있는 경우 리스트 렌더링
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
                    songLiked: review.songLiked, // 초기 상태
                    reviewLiked: review.reviewLiked, // 초기 상태
                    onSongLikePressed: () async {
                      setState(() {
                        review.songLiked = !review.songLiked;
                      });
                      try {
                        await SongService.likeSong(
                            widget.username, review.title, review.artist);
                      } catch (e) {
                        showErrorDialog(context, "이미 좋아요한 노래입니다");
                      }
                    },
                    onReviewLikePressed: () async {
                      setState(() {
                        review.reviewLiked = !review.reviewLiked;
                      });
                      try {
                        String formattedDate =
                            DateFormat('yyyy-MM-dd').format(review.reviewDate);

                        await ReviewService.likeReview(
                            widget.username, formattedDate, review.username);
                      } catch (e) {
                        showErrorDialog(context, "이미 좋아요한 리뷰입니다");
                      }
                    },
                  ),
                );
              },
            );
          } else {
            // 데이터가 없는 경우 기본 처리
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
