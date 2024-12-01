import 'package:flutter/material.dart';
import 'package:musicdiary/Model/song_model.dart';
import 'package:musicdiary/Screen/Today_Music_Screen/upload_song_page.dart';
import 'package:musicdiary/Service/song_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';

class FindMusicPage extends StatefulWidget {
  const FindMusicPage({super.key});

  @override
  State<FindMusicPage> createState() => _FindMusicPageState();
}

class _FindMusicPageState extends State<FindMusicPage> {
  final TextEditingController _songNameController = TextEditingController();
  final TextEditingController _artistNameController = TextEditingController();
  Future<SongModel>? song; // 검색된 노래 정보

  void _searchMusic() async {
    String songName = _songNameController.text.trim();
    String artistName = _artistNameController.text.trim();

    if (songName.isNotEmpty && artistName.isNotEmpty) {
      try {
        final result = await SongService.getSong(songName, artistName);
        setState(() {
          song = Future.value(result); // 검색 결과를 저장
        });
        Navigator.pop(context, song);
      } catch (e) {
        print("오류 발생: $e");
        showErrorDialog(context, "해당 노래가 존재하지 않습니다.");
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("노래 제목 또는 가수를 입력하세요.")),
      );
    }
  }

  void _goToUploadSongPage() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => const UploadSongPage()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("음악 검색"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              "노래 제목",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _songNameController,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: "노래 제목을 입력하세요.",
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              "가수 이름",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _artistNameController,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: "가수 이름을 입력하세요.",
              ),
            ),
            const SizedBox(height: 16),
            Center(
              child: ElevatedButton(
                onPressed: _searchMusic,
                child: const Text("검색"),
              ),
            ),
            const SizedBox(height: 40), // 검색 버튼과 음악 추가하기 버튼 사이 여백
            Center(
              child: ElevatedButton(
                onPressed: _goToUploadSongPage,
                child: const Text("음악 추가하기"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
