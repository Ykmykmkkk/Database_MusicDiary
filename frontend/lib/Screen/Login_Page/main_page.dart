import 'package:flutter/material.dart';
import 'package:musicdiary/Screen/Today_Music_Screen/today_music_page.dart';
import 'package:musicdiary/Screen/Music_Calender_Screen/music_diary_page.dart';
import 'package:musicdiary/Screen/My_Page_Scree/my_page.dart';
import 'package:musicdiary/Screen/Review_Community_Screen/review_page.dart';

class MainPage extends StatefulWidget {
  final String username;
  const MainPage({super.key, required this.username});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  int _selectedIndex = 0;
  late String username;
  late List<Widget> screens;

  @override
  void initState() {
    super.initState();
    username = widget.username;
    screens = [
      TodayMusicPage(username: username),
      MusicDiaryPage(username: username),
      ReviewPage(username: username),
      MyPage(username: username),
    ];
  }

  void _changeTab(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    Size screenSize = MediaQuery.of(context).size;
    double screenHeight = screenSize.height;

    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.surface,
      body: screens[_selectedIndex],
      bottomNavigationBar: NavigationBar(
        height: screenHeight * 0.1,
        backgroundColor: Theme.of(context).primaryColorLight,
        animationDuration: const Duration(seconds: 1),
        selectedIndex: _selectedIndex,
        onDestinationSelected: (index) {
          setState(() {
            _selectedIndex = index;
          });
        },
        destinations: _navBarItems,
        indicatorColor: Theme.of(context).primaryColor,
      ),
    );
  }

  final List<Widget> _navBarItems = [
    const NavigationDestination(
      icon: Icon(Icons.library_music_outlined),
      selectedIcon: Icon(Icons.library_music_outlined),
      label: '오늘의음악',
    ),
    const NavigationDestination(
      icon: Icon(Icons.calendar_month_outlined),
      selectedIcon: Icon(Icons.calendar_month_outlined),
      label: '음악캘린더',
    ),
    const NavigationDestination(
      icon: Icon(Icons.rate_review_outlined),
      selectedIcon: Icon(Icons.rate_review_outlined),
      label: '리뷰게시판',
    ),
    const NavigationDestination(
      icon: Icon(Icons.person_rounded),
      selectedIcon: Icon(Icons.person_rounded),
      label: '마이페이지',
    ),
  ];
}