import 'package:flutter/material.dart';
import 'package:musicdiary/Provider/UserProvider.dart';
import 'package:musicdiary/Screen/Login_Page/login_page.dart';
import 'package:musicdiary/Screen/My_Page_Scree/all_review_page.dart';
import 'package:musicdiary/Screen/My_Page_Scree/liked_reviews_page.dart';
import 'package:musicdiary/Screen/My_Page_Scree/liked_songs_page.dart';
import 'package:musicdiary/Service/auth_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';
import 'package:provider/provider.dart';

class MyPage extends StatefulWidget {
  final String userId;
  final String username;
  const MyPage({super.key, required this.userId, required this.username});

  @override
  State<MyPage> createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  late String userId;
  late String username;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    userId = widget.userId;
    username = widget.username;
  }

  void _logout(BuildContext context) async {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text("로그아웃"),
        content: const Text("정말 로그아웃하시겠습니까?"),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(), // 다이얼로그 닫기
            child: const Text("취소"),
          ),
          TextButton(
            onPressed: () {
              Navigator.of(context).pop(); // 다이얼로그 닫기
              Navigator.pushAndRemoveUntil(
                  context,
                  MaterialPageRoute(builder: (context) => const LoginPage()),
                  (route) => false); // 로그인 화면으로 이동
            },
            child: const Text("확인"),
          ),
        ],
      ),
    );
  }

  void _deleteAccount(BuildContext context) {
    // 회원 탈퇴 로직 구현 (예: 사용자 계정 삭제 API 호출 등)
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text("회원 탈퇴"),
        content: const Text("정말로 회원 탈퇴하시겠습니까?"),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text("취소"),
          ),
          TextButton(
            onPressed: () async {
              Navigator.of(context).pop();
              // 회원 탈퇴 후 처리 (예: 초기 화면으로 이동)
              try {
                await AuthService.delete();
              } catch (e) {
                showErrorDialog(context, "회원 탈퇴에 실패했습니다. 다시 시도해주세요");
              }
              Navigator.pushAndRemoveUntil(
                  context,
                  MaterialPageRoute(builder: (context) => const LoginPage()),
                  (route) => false); // 로그인 화면으로 이동
            },
            child: const Text("확인"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          "마이 페이지",
        ),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // 사용자 이름 (또는 제목)
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              "$username님", // 사용자 이름 예시
              style: const TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
              ),
              textAlign: TextAlign.center,
            ),
          ),
          const SizedBox(height: 16),
          // 메뉴 영역
          Expanded(
            child: ListView(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              children: [
                Card(
                  elevation: 4,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12.0),
                  ),
                  child: ListTile(
                    leading: const Icon(
                      Icons.comment_outlined,
                      color: Colors.indigoAccent,
                    ),
                    title: const Text(
                      "작성한 감상평",
                      style:
                          TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    trailing: const Icon(Icons.arrow_forward_ios),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) => AllReviewPage(
                                userId: userId, username: username)),
                      );
                    },
                  ),
                ),
                const SizedBox(height: 16),
                Card(
                  elevation: 4,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12.0),
                  ),
                  child: ListTile(
                    leading: const Icon(
                      Icons.music_video_outlined,
                      color: Colors.deepOrangeAccent,
                    ),
                    title: const Text(
                      "좋아요한 노래",
                      style:
                          TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    trailing: const Icon(Icons.arrow_forward_ios),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) =>
                                LikedSongsPage(username: username)),
                      );
                    },
                  ),
                ),
                const SizedBox(height: 16),
                Card(
                  elevation: 4,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12.0),
                  ),
                  child: ListTile(
                    leading: const Icon(
                      Icons.favorite,
                      color: Colors.red,
                    ),
                    title: const Text(
                      "좋아요한 감상평",
                      style:
                          TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    trailing: const Icon(Icons.arrow_forward_ios),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) =>
                                LikedReviewsPage(username: username)),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 16),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            child: Row(
              children: [
                // 로그아웃 버튼
                Expanded(
                  child: ElevatedButton(
                    onPressed: () => _logout(context),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.indigoAccent,
                      padding: const EdgeInsets.symmetric(vertical: 16.0),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                    ),
                    child: const Text(
                      "로그아웃",
                      style:
                          TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                // 회원 탈퇴 버튼
                Expanded(
                  child: ElevatedButton(
                    onPressed: () => _deleteAccount(context),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.redAccent,
                      padding: const EdgeInsets.symmetric(vertical: 16.0),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                    ),
                    child: const Text(
                      "회원 탈퇴",
                      style:
                          TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                    ),
                  ),
                ),
              ],
            ),
          ),

          const SizedBox(height: 16),
        ],
      ),
    );
  }
}
