import 'package:flutter/material.dart';

class UserProvider with ChangeNotifier {
  String? _userId;
  String? _username;

  String? get userId => _userId;
  String? get username => _username;

  void setUserData(String userId, String username) {
    _userId = userId;
    _username = username;
    notifyListeners();
  }

  void clearUserData() {
    _userId = null;
    _username = null;
    notifyListeners();
  }
}
