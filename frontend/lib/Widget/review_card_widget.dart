import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:musicdiary/Model/review_model.dart';

class ReviewCardWidget extends StatelessWidget {
  final ReviewModel reviewData;
  final String username;
  final bool songLiked;
  final bool reviewLiked;
  final VoidCallback onSongLikePressed;
  final VoidCallback onReviewLikePressed;

  const ReviewCardWidget({
    super.key,
    required this.reviewData,
    required this.username,
    required this.songLiked,
    required this.reviewLiked,
    required this.onSongLikePressed,
    required this.onReviewLikePressed,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(16),
      ),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  DateFormat('yyyy-MM-dd').format(reviewData.reviewDate),
                  style: const TextStyle(
                    fontSize: 16,
                    color: Colors.grey,
                  ),
                ),
                Chip(
                  label: Text(
                    reviewData.isPublic ? "Public" : "Private",
                    style: const TextStyle(color: Colors.white),
                  ),
                  backgroundColor:
                      reviewData.isPublic ? Colors.green : Colors.red,
                ),
              ],
            ),
            const SizedBox(height: 16),
            Card(
              elevation: 2,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Row(
                      children: [
                        const Icon(Icons.music_note, color: Colors.deepPurple),
                        const SizedBox(width: 8),
                        Text.rich(
                          TextSpan(
                            text: reviewData.songTitle,
                            style: const TextStyle(
                              fontSize: 20,
                              fontWeight: FontWeight.bold,
                            ),
                            children: [
                              TextSpan(
                                text: " - ${reviewData.songArtist}",
                                style: const TextStyle(
                                  fontSize: 16,
                                  color: Colors.grey,
                                  fontWeight: FontWeight.normal,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    IconButton(
                      icon: Icon(
                        songLiked ? Icons.favorite : Icons.favorite_border,
                        color: Colors.pink,
                      ),
                      onPressed: onSongLikePressed,
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              "감상평",
              style: TextStyle(fontSize: 26, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            Text(
              reviewData.reviewContent,
              style: const TextStyle(fontSize: 18),
            ),
            const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  "Review by: $username",
                  style: const TextStyle(
                    fontSize: 16,
                    color: Colors.grey,
                  ),
                ),
                IconButton(
                  icon: Icon(
                    reviewLiked ? Icons.favorite : Icons.favorite_border,
                    color: reviewLiked ? Colors.pink : Colors.grey,
                  ),
                  onPressed: onReviewLikePressed,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
