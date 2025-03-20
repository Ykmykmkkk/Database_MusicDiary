import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:intl/intl.dart';
import 'package:musicdiary/Provider/UserProvider.dart';
import 'package:musicdiary/Screen/Login_Page/login_page.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  Intl.defaultLocale = 'ko_KR'; // 기본 로케일 설정
  await dotenv.load(fileName: "assets/config/.env");
  SharedPreferences prefs = await SharedPreferences.getInstance();

  UserProvider userProvider = UserProvider();
  List<String>? token = prefs.getStringList('token');
  runApp(ChangeNotifierProvider(
    create: (context) => userProvider,
    child: const MyApp(),
  ));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      localizationsDelegates: const [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: const [
        Locale('ko', 'KR'), // 한국어
        Locale('en', 'US'), // 영어
      ],
      locale: const Locale('ko', 'KR'),
      home: const LoginPage(),
      theme: ThemeData(
        appBarTheme: const AppBarTheme(
          systemOverlayStyle: SystemUiOverlayStyle(
            statusBarColor: Color.fromARGB(255, 82, 180, 139),
            statusBarIconBrightness: Brightness.dark,
          ),
        ),
      ),
      builder: (context, child) {
        return child!;
      },
    );
  }
}
