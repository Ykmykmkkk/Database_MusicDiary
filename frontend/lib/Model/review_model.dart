class ReviewModel {
  final String username; // 사용자 이름
  final String title; // 노래 제목
  final String artist; // 아티스트 정보
  final DateTime reviewDate; // 리뷰 날짜
  final String reviewContent; // 리뷰 내용
  final bool isPublic; // 리뷰 공개 여부
  bool songLiked;
  bool reviewLiked;
  ReviewModel(
      {required this.username,
      required this.title,
      required this.artist,
      required this.reviewDate,
      required this.reviewContent,
      required this.isPublic,
      this.songLiked = false,
      this.reviewLiked = false});
  // JSON 데이터를 Dart 객체로 변환 (역직렬화)

  factory ReviewModel.fromJson(Map<String, dynamic> json) {
    return ReviewModel(
      username: json['username'],
      title: json['title'],
      artist: json['artist'],
      reviewDate: DateTime.parse(json['reviewDate']),
      reviewContent: json['reviewContent'],
      isPublic: json['isPublic'],
    );
  }

  // Dart 객체를 JSON 데이터로 변환 (직렬화)
  Map<String, dynamic> toJson() {
    return {
      'username': username,
      'title': title,
      'artist': artist,
      'reviewDate': reviewDate.toIso8601String(),
      'reviewContent': reviewContent,
      'isPublic': isPublic,
    };
  }
}
