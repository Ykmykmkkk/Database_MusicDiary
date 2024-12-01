import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class SongCardWidget extends StatelessWidget {
  final String title; // 노래 제목
  final String album; // 앨범 제목
  final String artist; // 아티스트 정보
  final DateTime releaseDate; // 발매일
  final String durationTime; // 노래 재생 시간
  final bool isLiked; // 좋아요 상태
  final VoidCallback onLikePressed; // 좋아요 버튼 동작

  const SongCardWidget({
    super.key,
    required this.title,
    required this.album,
    required this.artist,
    required this.releaseDate,
    required this.durationTime,
    required this.isLiked,
    required this.onLikePressed,
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
            // 노래 제목과 좋아요 버튼
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 22,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                IconButton(
                  icon: Icon(
                    isLiked ? Icons.favorite : Icons.favorite_border,
                    color: isLiked ? Colors.pink : Colors.grey,
                  ),
                  onPressed: onLikePressed,
                ),
              ],
            ),
            const SizedBox(height: 8),
            // 앨범 제목 및 아티스트 정보
            Row(
              children: [
                const Icon(Icons.album, color: Colors.deepPurple),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    album,
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 4),
            Row(
              children: [
                const Icon(Icons.person, color: Colors.blueAccent),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    artist,
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            // 발매일과 재생 시간
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Row(
                  children: [
                    const Icon(Icons.calendar_today,
                        size: 18, color: Colors.grey),
                    const SizedBox(width: 8),
                    Text(
                      DateFormat('yyyy-MM-dd').format(releaseDate),
                      style: const TextStyle(
                        fontSize: 14,
                        color: Colors.grey,
                      ),
                    ),
                  ],
                ),
                Row(
                  children: [
                    const Icon(Icons.timer, size: 18, color: Colors.grey),
                    const SizedBox(width: 8),
                    Text(
                      durationTime,
                      style: const TextStyle(
                        fontSize: 14,
                        color: Colors.grey,
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
