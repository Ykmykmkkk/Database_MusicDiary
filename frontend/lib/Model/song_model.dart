class SongModel {
  final String songId;
  final String title; // 노래 제목
  final String album; // 앨범 제목
  final String artist; // 아티스트 정보
  final DateTime releaseDate; // 발매일
  final String durationTime; // 노래 재생 시간
  bool isLiked;
  // 생성자
  SongModel(
      {required this.songId,
      required this.title,
      required this.album,
      required this.artist,
      required this.releaseDate,
      required this.durationTime,
      this.isLiked = false});

  // JSON 데이터에서 객체 생성 (역직렬화)
  factory SongModel.fromJson(Map<String, dynamic> json) {
    return SongModel(
      songId: json['id'] ?? 'Unknown Id',
      title: json['title'] ?? 'Unknown Title',
      album: json['album'] ?? 'Unknown Album',
      artist: json['artist'] ?? 'Unknown Artist',
      releaseDate: json['releaseDate'] != null
          ? DateTime.parse(json['releaseDate']) // JSON 키 수정
          : DateTime.now(), // 기본값으로 현재 날짜 사용
      durationTime: json['durationTime'] ?? 'Unknown Duration',
    );
  }

  // 객체를 JSON 데이터로 변환 (직렬화)
  Map<String, dynamic> toJson() {
    return {
      'title': title,
      'album': album,
      'artist': artist,
      'releaseDate':
          releaseDate.toIso8601String(), // DateTime을 ISO 8601 형식으로 변환
      'durationTime': durationTime,
    };
  }
}
