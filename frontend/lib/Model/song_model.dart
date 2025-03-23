class SongModel {
  final int songId;
  final String songTitle; // 노래 제목
  final String songAlbum; // 앨범 제목
  final String songArtist; // 아티스트 정보
  final DateTime releaseDate; // 발매일
  final String durationTime; // 노래 재생 시간
  bool isLiked;

  // 생성자
  SongModel({
    required this.songId,
    required this.songTitle,
    required this.songAlbum,
    required this.songArtist,
    required this.releaseDate,
    required this.durationTime,
    this.isLiked = false,
  });

  // JSON 데이터에서 객체 생성 (역직렬화)
  factory SongModel.fromJson(Map<String, dynamic> json) {
    return SongModel(
      songId: json['id'] ?? 0, // 기본값 0으로 설정
      songTitle: json['title'] ?? 'Unknown Title', // JSON 키 확인 필요
      songAlbum: json['album'] ?? 'Unknown Album',
      songArtist: json['artist'] ?? 'Unknown Artist',
      releaseDate: json['releaseDate'] != null
          ? DateTime.parse(json['releaseDate'])
          : DateTime.now(), // 기본값으로 현재 날짜 사용
      durationTime: json['durationTime'] ?? 'Unknown Duration',
    );
  }

  // 객체를 JSON 데이터로 변환 (직렬화)
  Map<String, dynamic> toJson() {
    return {
      'id': songId, // songId 추가
      'title': songTitle,
      'album': songAlbum,
      'artist': songArtist,
      'releaseDate':
          releaseDate.toIso8601String(), // DateTime을 ISO 8601 형식으로 변환
      'durationTime': durationTime,
    };
  }
}
