import 'package:flutter/material.dart';
import 'package:musicdiary/Model/song_model.dart';
import 'package:musicdiary/Service/song_service.dart';
import 'package:musicdiary/Widget/song_card_widget.dart';

class LikedSongsPage extends StatefulWidget {
  final String username;
  const LikedSongsPage({super.key, required this.username});

  @override
  State<LikedSongsPage> createState() => _LikedSongsPageState();
}

class _LikedSongsPageState extends State<LikedSongsPage> {
  late Future<List<SongModel>> likedSongs;
  List<SongModel> currentSongs = []; // 현재 화면에 표시된 노래 리스트

  @override
  void initState() {
    super.initState();
    // 좋아요한 노래 데이터를 서버 또는 데이터베이스에서 가져옴
    _fetchLikedSongs();
  }

  // 데이터를 불러오고 currentSongs에 저장
  void _fetchLikedSongs() async {
    likedSongs = SongService.getLikedSongs();
    likedSongs.then((songs) {
      setState(() {
        currentSongs = songs;
      });
    }).catchError((error) {
      print("Error fetching liked songs: $error");
    });
  }

  // 좋아요 취소 함수
  void _unlikeSong(SongModel song) async {
    try {
      await SongService.unlikeSong(song.songId);
      setState(() {
        currentSongs.remove(song); // 리스트에서 제거
      });
    } catch (e) {
      print("Error unliking song: $e");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "${widget.username}님의 좋아요한 노래",
          style: const TextStyle(fontSize: 18),
        ),
      ),
      body: currentSongs.isEmpty
          ? const Center(
              child: Text(
                "좋아요한 노래가 없습니다.",
                style: TextStyle(fontSize: 18, color: Colors.grey),
              ),
            )
          : ListView.builder(
              padding: const EdgeInsets.all(16.0),
              itemCount: currentSongs.length,
              itemBuilder: (context, index) {
                final song = currentSongs[index];
                return Padding(
                  padding: const EdgeInsets.only(bottom: 16.0),
                  child: SongCardWidget(
                    title: song.title,
                    album: song.album,
                    artist: song.artist,
                    releaseDate: song.releaseDate,
                    durationTime: song.durationTime,
                    isLiked: true,
                    onLikePressed: () => _unlikeSong(song), // 좋아요 취소
                  ),
                );
              },
            ),
    );
  }
}
