import 'package:flutter/material.dart';
import 'package:musicdiary/Screen/Music_Calender_Screen/day_review_page.dart';

class MusicDiaryPage extends StatefulWidget {
  final String username;
  const MusicDiaryPage({super.key, required this.username});

  @override
  State<MusicDiaryPage> createState() => _MusicDiaryPageState();
}

class _MusicDiaryPageState extends State<MusicDiaryPage> {
  DateTime _selectedDate = DateTime.now();
  late String username;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    username = widget.username;
  }

  void _lastMonth() {
    setState(() {
      _selectedDate = DateTime(_selectedDate.year, _selectedDate.month - 1);
    });
  }

  void _nextMonth() {
    setState(() {
      _selectedDate = DateTime(_selectedDate.year, _selectedDate.month + 1);
    });
  }

  List<DateTime> _getDaysInMonth(DateTime date) {
    DateTime firstDayOfMonth = DateTime(date.year, date.month, 1);
    DateTime lastDayOfMonth = DateTime(date.year, date.month + 1, 0);

    int previousDays = firstDayOfMonth.weekday - 1; // 월요일 기준(1)
    int remainingDays = 7 - lastDayOfMonth.weekday;

    List<DateTime> days = [];
    for (int i = -previousDays; i <= lastDayOfMonth.day + remainingDays; i++) {
      days.add(DateTime(date.year, date.month, i));
    }
    return days;
  }

  void _onDayTap(DateTime day) {
    if (day.month == _selectedDate.month) {
      // 현재 월의 날짜만 처리
      Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) =>
                  DayReviewPage(username: username, day: day)));
    }
  }

  @override
  Widget build(BuildContext context) {
    List<DateTime> days = _getDaysInMonth(_selectedDate);

    return Scaffold(
      appBar: AppBar(
        title: Text("${_selectedDate.year}년 ${_selectedDate.month}월"),
        centerTitle: true,
      ),
      body: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              IconButton(
                onPressed: _lastMonth,
                icon: const Icon(Icons.keyboard_arrow_left),
              ),
              Text(
                "${_selectedDate.month}월",
                style: const TextStyle(
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
              IconButton(
                onPressed: _nextMonth,
                icon: const Icon(Icons.keyboard_arrow_right),
              ),
            ],
          ),
          Expanded(
            child: GridView.builder(
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 7, // 7열 (일~토)
                childAspectRatio: 0.8, // 셀의 높이를 늘리기 위해 변경
              ),
              itemCount: days.length,
              itemBuilder: (context, index) {
                DateTime day = days[index];
                bool isCurrentMonth = day.month == _selectedDate.month;

                return GestureDetector(
                  onTap: () => _onDayTap(day),
                  child: Container(
                    margin: const EdgeInsets.all(4),
                    decoration: BoxDecoration(
                      color: isCurrentMonth ? Colors.white : Colors.grey[200],
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(
                        color:
                            isCurrentMonth ? Colors.grey : Colors.transparent,
                      ),
                    ),
                    child: Center(
                      child: Text(
                        "${day.day}",
                        style: TextStyle(
                          color: isCurrentMonth ? Colors.black : Colors.grey,
                          fontWeight: isCurrentMonth
                              ? FontWeight.bold
                              : FontWeight.normal,
                          fontSize: 18,
                        ),
                      ),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
