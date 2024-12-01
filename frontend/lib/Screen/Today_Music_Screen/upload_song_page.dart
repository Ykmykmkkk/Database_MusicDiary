import 'package:flutter/material.dart';
import 'package:musicdiary/Service/song_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';

class UploadSongPage extends StatefulWidget {
  const UploadSongPage({super.key});

  @override
  State<UploadSongPage> createState() => _UploadSongPageState();
}

class _UploadSongPageState extends State<UploadSongPage> {
  final TextEditingController _songTitleController = TextEditingController();
  final TextEditingController _albumNameController = TextEditingController();
  final TextEditingController _artistNameController = TextEditingController();
  final TextEditingController _releaseDateController = TextEditingController();
  final TextEditingController _durationTimeController = TextEditingController();

  void _submitSong() async {
    String songTitle = _songTitleController.text.trim();
    String albumName = _albumNameController.text.trim();
    String artistName = _artistNameController.text.trim();
    String releaseDate = _releaseDateController.text.trim();
    String durationTime = _durationTimeController.text.trim();

    if (songTitle.isEmpty ||
        albumName.isEmpty ||
        artistName.isEmpty ||
        releaseDate.isEmpty ||
        durationTime.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("모든 필드를 입력해주세요.")),
      );
      return;
    }

    try {
      await SongService.createSong(
          songTitle, albumName, artistName, releaseDate, durationTime);

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("노래가 성공적으로 추가되었습니다!")),
      );
    } catch (e) {
      print("오류잡았다");
      showErrorDialog(context, "해당 음악은 이미 존재합니다");
    }
    // 입력 필드 초기화
    _songTitleController.clear();
    _albumNameController.clear();
    _artistNameController.clear();
    _releaseDateController.clear();
    _durationTimeController.clear();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("노래 추가하기"),
      ),
      body: SingleChildScrollView(
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
              controller: _songTitleController,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: "노래 제목을 입력하세요.",
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              "앨범명",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _albumNameController,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: "앨범명을 입력하세요.",
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              "아티스트 이름",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _artistNameController,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: "아티스트 이름을 입력하세요.",
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              "발매일",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _releaseDateController,
              keyboardType: TextInputType.datetime,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: "발매일을 YYYY-MM-DD 형식으로 입력하세요.",
              ),
            ),
            const SizedBox(height: 16),
            const Text(
              "재생 시간",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            TextField(
              controller: _durationTimeController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                hintText: "재생 시간을 입력하세요. (예: 3:45)",
              ),
            ),
            const SizedBox(height: 32),
            Center(
              child: ElevatedButton(
                onPressed: _submitSong,
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(
                      horizontal: 40.0, vertical: 15.0),
                ),
                child: const Text(
                  "노래 추가",
                  style: TextStyle(fontSize: 18),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
