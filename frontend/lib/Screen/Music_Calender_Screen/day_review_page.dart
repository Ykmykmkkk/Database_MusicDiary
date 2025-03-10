import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:musicdiary/Model/review_model.dart';
import 'package:musicdiary/Model/song_model.dart';
import 'package:musicdiary/Service/review_service.dart';
import 'package:musicdiary/Service/song_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';
import 'package:musicdiary/Widget/review_card_widget.dart';
import 'package:musicdiary/Service/review_service.dart';

class DayReviewPage extends StatefulWidget {
  final DateTime day;

  const DayReviewPage({super.key, required this.day});

  @override
  State<DayReviewPage> createState() => _DayReviewPageState();
}

class _DayReviewPageState extends State<DayReviewPage> {
  late Future<ReviewModel> review;

  @override
  void initState() {
    super.initState();
    String formattedDate = DateFormat('yyyy-MM-dd').format(widget.day);
    review = ReviewService.getReviewDate(formattedDate);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          DateFormat('MMMM dd일, yyyy').format(widget.day),
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.deepPurple,
      ),
      body: FutureBuilder<ReviewModel>(
        future: review,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return const Center(
              child: Text(
                "작성한 감상평이 없습니다",
                style: TextStyle(color: Colors.grey, fontSize: 20),
                textAlign: TextAlign.center,
              ),
            );
          } else if (snapshot.hasData) {
            final reviewData = snapshot.data!;
            return FutureBuilder<bool>(
              future: SongService.isLikedSong(reviewData.songId),
              builder: (context, songSnapshot) {
                if (songSnapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                }
                bool songLiked = songSnapshot.data ?? false;
                return Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: SingleChildScrollView(
                    child: ReviewCardWidget(
                      reviewData: reviewData,
                      username: reviewData.writerUsername,
                      songLiked: songLiked,
                      reviewLiked: reviewData.isLike,
                      onSongLikePressed: () async {
                        setState(() {
                          songLiked = !songLiked;
                        });
                        try {
                          await SongService.likeSong(reviewData.songArtist);
                        } catch (e) {
                          showMessageDialog(context, "이미 좋아요한 노래입니다");
                        }
                      },
                      onReviewLikePressed: () async {
                        setState(() {
                          reviewData.isLike = !reviewData.isLike;
                        });
                        try {
                          String formattedDate = DateFormat('yyyy-MM-dd')
                              .format(reviewData.reviewDate);

                          if (reviewData.isLike) {
                            await ReviewService.likeReview(formattedDate);
                          } else {
                            await ReviewService.unlikeReview(formattedDate);
                          }
                        } catch (e) {
                          showMessageDialog(context, "이미 좋아요한 리뷰입니다.");
                        }
                      },
                    ),
                  ),
                );
              },
            );
          } else {
            return const Center(
              child: Text(
                "No review found for this date.",
                style: TextStyle(fontSize: 18, color: Colors.grey),
              ),
            );
          }
        },
      ),
    );
  }
}
