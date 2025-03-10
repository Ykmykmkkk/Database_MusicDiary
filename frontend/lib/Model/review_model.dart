class ReviewModel {
  final String reviewId;
  final String writerId;
  final String writerUsername; // 사용자 이름
  final String songId; //
  final String songTitle; // 노래 제목
  final String songArtist; // 아티스트 정보
  final DateTime reviewDate; // 리뷰 날짜
  final String reviewTitle; // 리뷰 제목
  final String reviewContent; // 리뷰 내용
  final bool isPublic; // 리뷰 공개 여부
  bool isLike;
  ReviewModel(
      {required this.reviewId,
      required this.writerId,
      required this.writerUsername,
      required this.songId,
      required this.songTitle,
      required this.songArtist,
      required this.reviewDate,
      required this.reviewTitle,
      required this.reviewContent,
      required this.isPublic,
      this.isLike = false});
  // JSON 데이터를 Dart 객체로 변환 (역직렬화)

  // JSON 데이터를 Dart 객체로 변환 (역직렬화)
  factory ReviewModel.fromJson(Map<String, dynamic> json) {
    return ReviewModel(
      reviewId: json['id'],
      writerId: json['writerId'],
      writerUsername: json['writerUsername'],
      songId: json['songId'],
      songTitle: json['songTitle'],
      songArtist: json['songArtist'],
      reviewDate: DateTime.parse(json['reviewDate']),
      reviewTitle: json['reviewTitle'],
      reviewContent: json['reviewContent'],
      isPublic: json['isPublic'],
      isLike: json['isLike'] ?? false, // 기본값 false
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
      'reviewDate': reviewDate.toIso8601String(),
      'reviewTitle': reviewTitle,
      'reviewContent': reviewContent,
      'isPublic': isPublic,
      'isLike': isLike,
    };
  }
}
