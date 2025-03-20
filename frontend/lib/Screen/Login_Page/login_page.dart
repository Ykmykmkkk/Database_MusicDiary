import 'package:flutter/material.dart';
import 'package:musicdiary/Provider/UserProvider.dart';
import 'package:musicdiary/Screen/Login_Page/main_page.dart';
import 'package:musicdiary/Screen/Login_Page/register_page.dart';
import 'package:musicdiary/Service/auth_service.dart';
import 'package:musicdiary/Widget/custom_dialog_widget.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  late TextEditingController controllerUsername, controllerPwd;
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    controllerUsername = TextEditingController();
    controllerPwd = TextEditingController();
  }

  void _login() async {
    // 로그인 버튼이 눌릴 시 호출되는 함수
    setState(() {
      isLoading = true;
    });
    var login = await AuthService.login(
      username: controllerUsername.text,
      password: controllerPwd.text,
    );
    setState(() {
      isLoading = false;
    });
    if (login) {
      if (!mounted) return; // UserProvider에 userId, username을 저장

      Navigator.pushAndRemoveUntil(
        context,
        MaterialPageRoute(builder: (context) => const MainPage()),
        (Route<dynamic> route) => false,
      );
    } else {
      if (!mounted) {
        return;
      }
      showErrorDialog(context, '아이디/Password가 틀렸습니다.');
    }
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        FocusScope.of(context).unfocus();
      },
      child: Scaffold(
        backgroundColor: Colors.blue[400]?.withOpacity(0.9),
        body: isLoading
            ? const Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Center(child: CircularProgressIndicator()),
                  SizedBox(
                    height: 20,
                  ),
                  Text(
                    '로그인 중..',
                    style: TextStyle(fontSize: 15, fontWeight: FontWeight.w500),
                  ),
                ],
              )
            : Padding(
                padding: const EdgeInsets.all(30),
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    mainAxisSize: MainAxisSize.min, // Column의 높이를 최소로 설정
                    children: [
                      const SizedBox(
                        height: 50,
                      ),
                      const Padding(
                        padding: EdgeInsets.symmetric(vertical: 15),
                        child: Text(
                          'MusicDiary',
                          style: TextStyle(
                            fontSize: 50,
                            color: Color.fromARGB(255, 82, 59, 42),
                            fontFamily: "Logo",
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(vertical: 20),
                        child: Image.asset(
                          'assets/logo.png', // 로고 이미지 경로
                          height: 150,
                        ),
                      ),
                      const SizedBox(
                        height: 50,
                      ),
                      Column(
                        children: [
                          Container(
                            height: 55,
                            width: 400,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(70),
                            ),
                            child: TextField(
                              decoration: InputDecoration(
                                labelText: 'ID',
                                filled: true,
                                fillColor: Colors.white,
                                border: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(20.0),
                                  borderSide: BorderSide.none,
                                ),
                              ),
                              autofocus: false,
                              controller: controllerUsername,
                            ),
                          ),
                          const SizedBox(
                            height: 10,
                          ),
                          Container(
                            height: 55,
                            width: 400,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(70),
                            ),
                            child: TextField(
                              decoration: InputDecoration(
                                labelText: 'Password',
                                filled: true,
                                fillColor: Colors.white,
                                border: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(20.0),
                                  borderSide: BorderSide.none,
                                ),
                              ),
                              obscureText: true,
                              autofocus: false,
                              controller: controllerPwd,
                            ),
                          ),
                          const SizedBox(
                            height: 18,
                          ),
                          SizedBox(
                            width: 300.0,
                            height: 50.0,
                            child: ElevatedButton(
                              style: ButtonStyle(
                                backgroundColor: WidgetStateProperty.all(
                                    const Color.fromARGB(255, 222, 234, 187)),
                              ),
                              onPressed: _login,
                              child: const Text(
                                '로그인',
                                style: TextStyle(
                                  color: Colors.black87,
                                  fontFamily: "MainButton",
                                  fontSize: 17,
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(
                            height: 13,
                          ),
                          SizedBox(
                            width: 300.0,
                            height: 50.0,
                            child: ElevatedButton(
                              style: ButtonStyle(
                                backgroundColor: WidgetStateProperty.all(
                                    const Color.fromARGB(255, 173, 190, 122)),
                              ),
                              onPressed: () {},
                              child: const Row(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Icon(
                                    Icons.account_circle_rounded,
                                    color: Colors.black,
                                  ),
                                  Text(
                                    '구글 계정으로 로그인',
                                    style: TextStyle(
                                      color: Colors.black87,
                                      fontFamily: "MainButton",
                                      fontSize: 17,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                          const SizedBox(
                            height: 10,
                          ),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              TextButton(
                                onPressed: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const RegisterPage()));
                                },
                                style: TextButton.styleFrom(
                                  foregroundColor: Colors.black,
                                ),
                                child: const Text('회원 가입'),
                              ),
                              const SizedBox(
                                width: 10,
                              ),
                              const Text('|'),
                              const SizedBox(
                                width: 10,
                              ),
                              TextButton(
                                onPressed: () {},
                                style: TextButton.styleFrom(
                                  foregroundColor: Colors.black,
                                ),
                                child: const Text('ID/Password 찾기'),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
      ),
    );
  }
}
