class ReviewModel {
  final int reviewId;
  final String writerId;
  final String writerUsername; // 사용자 이름
  final int songId;
  final String songTitle; // 노래 제목
  final String songArtist; // 아티스트 정보
  bool songLiked; // final 제거 -> 변경 가능하게 수정
  final DateTime reviewDate; // 리뷰 날짜
  final String reviewTitle; // 리뷰 제목
  final String reviewContent; // 리뷰 내용
  final bool isPublic; // 리뷰 공개 여부
  bool reviewLiked; // final 제거 -> 변경 가능하게 수정

  ReviewModel({
    required this.reviewId,
    required this.writerId,
    required this.writerUsername,
    required this.songId,
    required this.songTitle,
    required this.songArtist,
    required this.songLiked,
    required this.reviewDate,
    required this.reviewTitle,
    required this.reviewContent,
    required this.isPublic,
    this.reviewLiked = false,
  });

  // JSON 데이터를 Dart 객체로 변환 (역직렬화)
  factory ReviewModel.fromJson(Map<String, dynamic> json) {
    return ReviewModel(
      reviewId: json['id'],
      writerId: json['writerId'],
      writerUsername: json['writerUsername'],
      songId: json['songId'],
      songTitle: json['songTitle'],
      songArtist: json['songArtist'],
      songLiked: json['songLiked'] ?? false, // ✅ 추가: 기본값 false
      reviewDate: DateTime.parse(json['reviewDate']),
      reviewTitle: json['reviewTitle'],
      reviewContent: json['reviewContent'],
      isPublic: json['isPublic'],
      reviewLiked: json['isLike'] ?? false, // 기본값 false
    );
  }

  // Dart 객체를 JSON 데이터로 변환 (직렬화)
  Map<String, dynamic> toJson() {
    return {
      'reviewId': reviewId,
      'writerId': writerId,
      'writerUsername': writerUsername,
      'songId': songId,
      'songTitle': songTitle,
      'songArtist': songArtist,
      'songLiked': songLiked, // ✅ 추가
      'reviewDate': reviewDate.toIso8601String(),
      'reviewTitle': reviewTitle,
      'reviewContent': reviewContent,
      'isPublic': isPublic,
      'isLike': reviewLiked,
    };
  }
}
