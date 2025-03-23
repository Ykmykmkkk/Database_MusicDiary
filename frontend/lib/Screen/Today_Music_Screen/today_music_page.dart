import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:musicdiary/Model/song_model.dart';
import 'package:musicdiary/Provider/UserProvider.dart';
import 'package:musicdiary/Service/review_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';
import 'package:provider/provider.dart';
import 'find_music_page.dart'; // FindMusicPage를 import

class TodayMusicPage extends StatefulWidget {
  final String userId;
  final String username;
  const TodayMusicPage(
      {super.key, required this.userId, required this.username});

  @override
  State<TodayMusicPage> createState() => _TodayMusicPageState();
}

class _TodayMusicPageState extends State<TodayMusicPage> {
  SongModel? selectedSong; // 선택된 노래 정보 저장
  bool isPublic = false;
  final TextEditingController _reviewTitleController =
      TextEditingController(); // ✅ 감상평 제목 컨트롤러 추가
  final TextEditingController _reviewController = TextEditingController();
  late String today;
  late String todayy;
  late String userId;

  @override
  void initState() {
    super.initState();
    DateTime now = DateTime.now();
    today = DateFormat('yyyy-MM-dd').format(now);
    todayy = DateFormat('yyyy/MM/dd E', 'ko_KR').format(now);
    userId = widget.userId;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("오늘의 음악"),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    todayy,
                    style: const TextStyle(
                        fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  ElevatedButton(
                    onPressed: () async {
                      final result = await Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) => const FindMusicPage()),
                      );

                      if (result != null && result is SongModel) {
                        setState(() {
                          selectedSong = result; // 반환된 데이터를 저장
                        });
                      }
                    },
                    child: const Text("음악찾기"),
                  ),
                ],
              ),
            ),
            // 제목/앨범/가수/발매일
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Container(
                padding: const EdgeInsets.all(16.0),
                decoration: BoxDecoration(
                  color: Colors.white,
                  border: Border.all(color: Colors.grey),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: selectedSong != null
                    ? Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text("제목: ${selectedSong!.songTitle}",
                              style: const TextStyle(fontSize: 16)),
                          const SizedBox(height: 8),
                          Text("앨범: ${selectedSong!.songAlbum}",
                              style: const TextStyle(fontSize: 16)),
                          const SizedBox(height: 8),
                          Text("가수: ${selectedSong!.songArtist}",
                              style: const TextStyle(fontSize: 16)),
                          const SizedBox(height: 8),
                          Text(
                            "발매일: ${selectedSong!.releaseDate.toLocal().toString().split(' ')[0]}",
                            style: const TextStyle(fontSize: 16),
                          ),
                          const SizedBox(height: 8),
                          Text("재생시간: ${selectedSong!.durationTime}",
                              style: const TextStyle(fontSize: 16)),
                        ],
                      )
                    : const Center(
                        child: Text(
                          "선택된 음악이 없습니다.\n음악을 검색해 추가하세요.",
                          style: TextStyle(fontSize: 16, color: Colors.grey),
                        ),
                      ),
              ),
            ),
            // 감상평 제목 입력
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text("감상평 제목",
                      style:
                          TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 8),
                  TextField(
                    controller: _reviewTitleController, // ✅ 감상평 제목 입력 필드
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      hintText: "감상평 제목을 입력하세요...",
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            // 감상평 입력 및 공개여부
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text("감상평",
                      style:
                          TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 8),
                  TextField(
                    controller: _reviewController,
                    maxLines: 4,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      hintText: "감상평을 입력하세요...",
                    ),
                  ),
                  const SizedBox(height: 8),
                  Row(
                    children: [
                      const Text("공개 여부", style: TextStyle(fontSize: 16)),
                      Checkbox(
                          value: isPublic,
                          onChanged: (value) {
                            setState(() {
                              isPublic = !isPublic;
                            });
                          }),
                    ],
                  ),
                ],
              ),
            ),
            // 저장 버튼
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: ElevatedButton(
                onPressed: () {
                  // 감상평이 비어있는지 확인
                  if (selectedSong == null) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("노래를 먼저 선택해주세요.")),
                    );
                    return;
                  }
                  if (_reviewTitleController.text.trim().isEmpty) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("감상평 제목을 입력해주세요.")),
                    );
                    return;
                  }
                  if (_reviewController.text.trim().isEmpty) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("감상평을 입력해주세요.")),
                    );
                    return;
                  }

                  try {
                    ReviewService.createReview(
                        today,
                        userId,
                        selectedSong!.songId,
                        _reviewTitleController.text, // ✅ 제목 추가
                        _reviewController.text,
                        isPublic);
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("감상평이 저장되었습니다.")),
                    );
                  } catch (e) {
                    showMessageDialog(context, "오늘의 감상평을 이미 작성하였습니다.");
                    print("오류발생: $e");
                  }

                  // 입력 필드 초기화
                  _reviewTitleController.clear();
                  _reviewController.clear();
                },
                child: const Text("저장"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
