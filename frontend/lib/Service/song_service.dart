import 'dart:convert';
import 'dart:ffi';

import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:musicdiary/Model/song_model.dart';
import 'package:musicdiary/Service/auth_service.dart';

class SongService {
  static final hostAddress = dotenv.env['API_ADDRESS'];
  static Future<SongModel> getSong(String title, String artist) async {
    try {
      var headers = {'Content-Type': 'application/json'};
      final response = await http.get(
          Uri.parse('http://$hostAddress:8000/song/$title/$artist'),
          headers: headers);
      if (response.statusCode == 200) {
        final responseBody = utf8.decode(response.bodyBytes); // UTF-8 디코딩
        Map<String, dynamic> data = jsonDecode(responseBody);
        print("API 응답 데이터: $responseBody");
        return SongModel.fromJson(data);
      } else {
        print('ServerMessage: ${response.body}');
        throw Exception('Failed to load song: ${response.statusCode}');
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<void> createSong(String title, String album, String artist,
      String releaseDate, String durationTime) async {
    var token = await AuthService.loadToken();
    var headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ${token[1]}'
    };
    var request =
        http.Request('POST', Uri.parse('http://$hostAddress:8000/song/create'));
    request.body = json.encode({
      "title": title,
      "album": album,
      "artist": artist,
      "releaseDate": releaseDate,
      "durationTime": durationTime,
    });
    request.headers.addAll(headers);
    try {
      http.StreamedResponse response = await request.send();
      String responseBody = await response.stream.bytesToString();
      if (response.statusCode == 201) {
        print("성공");
        return;
      } else {
        print("실패");
        print('ServerMessage: $responseBody');
        throw Exception("Failed to create song: ${response.statusCode}");
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<void> likeSong(String songId) async {
    var token = await AuthService.loadToken();
    var headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ${token[1]}'
    };
    var request = http.Request(
        'POST', Uri.parse('http://$hostAddress:8000/song/like/$songId'));
    request.headers.addAll(headers);
    try {
      http.StreamedResponse response = await request.send();
      String responseBody = await response.stream.bytesToString();
      if (response.statusCode == 200) {
        print("성공");
        return;
      } else {
        print("실패");
        print('ServerMessage: $responseBody');
        throw Exception("Failed to like song: ${response.statusCode}");
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<void> unlikeSong(String songId) async {
    var token = await AuthService.loadToken();
    var headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ${token[1]}'
    };
    var request = http.Request(
        'Delete', Uri.parse('http://$hostAddress:8000/song/like/$songId'));
    request.headers.addAll(headers);
    try {
      http.StreamedResponse response = await request.send();
      String responseBody = await response.stream.bytesToString();
      if (response.statusCode == 200) {
        print("unlike song 성공");
        return;
      } else {
        print("unlike song 실패");
        print('ServerMessage: $responseBody');
        throw Exception("Failed to like review: ${response.statusCode}");
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<List<SongModel>> getLikedSongs(String username) async {
    List<SongModel> songInstances = [];
    try {
      var token = await AuthService.loadToken();
      var headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${token[1]}'
      };
      final response = await http.get(
          Uri.parse('http://$hostAddress:8000/song/like/all'),
          headers: headers);
      if (response.statusCode == 200) {
        final responseBody = utf8.decode(response.bodyBytes); // UTF-8 디코딩
        List<dynamic> songList = jsonDecode(responseBody);
        if (songList.isNotEmpty) {
          for (var song in songList) {
            var instance = SongModel.fromJson(song);
            songInstances.add(instance);
          }
        }
        return songInstances;
      } else {
        print('ServerMessage: ${response.body}');
        throw Exception('Failed to load song: ${response.statusCode}');
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }
}
