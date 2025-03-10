import 'dart:convert';
import 'dart:ffi';

import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:musicdiary/Model/review_model.dart';
import 'package:musicdiary/Service/auth_service.dart';

class ReviewService {
  static final hostAddress = dotenv.env['API_ADDRESS'];

  static Future<void> createReview(
      String reviewDate,
      String username,
      String songTitle,
      String songArtist,
      String reviewContent,
      bool isPublic) async {
    var headers = {'Content-Type': 'application/json'};
    var request = http.Request(
        'POST', Uri.parse('http://$hostAddress:8000/review/create'));
    request.body = json.encode({
      "reviewDate": reviewDate,
      "username": username,
      "songTitle": songTitle,
      "songArtist": songArtist,
      "reviewContent": reviewContent,
      "isPublic": isPublic,
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

  static Future<ReviewModel> getReviewDate(String date) async {
    try {
      var token = await AuthService.loadToken();
      var headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${token[1]}'
      };
      final response = await http.get(
          Uri.parse('http://$hostAddress:8000/review/$date'),
          headers: headers);
      if (response.statusCode == 200) {
        final responseBody = utf8.decode(response.bodyBytes); // UTF-8 디코딩
        Map<String, dynamic> data = jsonDecode(responseBody);
        return ReviewModel.fromJson(data);
      } else {
        print('ServerMessage: ${response.body}');
        throw Exception('Failed to load review: ${response.statusCode}');
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<void> likeReview(String reviewId) async {
    var token = await AuthService.loadToken();
    var headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ${token[1]}'
    };
    var request = http.Request(
        'POST', Uri.parse('http://$hostAddress:8000/review/like/$reviewId'));
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
        throw Exception("Failed to like review: ${response.statusCode}");
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<void> unlikeReview(String reviewId) async {
    var token = await AuthService.loadToken();
    var headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ${token[1]}'
    };
    var request = http.Request('Delete',
        Uri.parse('http://$hostAddress:8000/review/unlike/$reviewId'));
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
        throw Exception("Failed to like review: ${response.statusCode}");
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<List<ReviewModel>> getLikedReviews() async {
    List<ReviewModel> reviewInstances = [];
    try {
      var token = await AuthService.loadToken();
      var headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${token[1]}'
      };
      final response = await http.get(
          Uri.parse('http://$hostAddress:8000/review/like/all'),
          headers: headers);
      if (response.statusCode == 200) {
        final responseBody = utf8.decode(response.bodyBytes); // UTF-8 디코딩
        List<dynamic> reviewList = jsonDecode(responseBody);
        if (reviewList.isNotEmpty) {
          for (var review in reviewList) {
            var instance = ReviewModel.fromJson(review);
            reviewInstances.add(instance);
          }
        }
        return reviewInstances;
      } else {
        print('ServerMessage: ${response.body}');
        throw Exception('Failed to load song: ${response.statusCode}');
      }
    } catch (e) {
      print(e);
      rethrow;
    }
  }

  static Future<List<ReviewModel>> getPublicReviews() async {
    List<ReviewModel> reviewInstances = [];
    try {
      var token = await AuthService.loadToken();
      var headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${token[1]}'
      };
      final response = await http.get(
          Uri.parse('http://$hostAddress:8000/review/public'),
          headers: headers);
      if (response.statusCode == 200) {
        final responseBody = utf8.decode(response.bodyBytes); // UTF-8 디코딩
        List<dynamic> reviewList = jsonDecode(responseBody);
        if (reviewList.isNotEmpty) {
          for (var review in reviewList) {
            var instance = ReviewModel.fromJson(review);
            reviewInstances.add(instance);
          }
        }
        return reviewInstances;
      } else {
        print('ServerMessage: ${response.body}');
        throw Exception('Failed to load song: ${response.statusCode}');
      }
    } catch (e) {
      return reviewInstances;
    }
  }

  static Future<List<ReviewModel>> getAllReviews(String username) async {
    List<ReviewModel> reviewInstances = [];
    try {
      var token = await AuthService.loadToken();
      var headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ${token[1]}'
      };
      final response = await http.get(
          Uri.parse('http://$hostAddress:8000/review/all'),
          headers: headers);
      if (response.statusCode == 200) {
        final responseBody = utf8.decode(response.bodyBytes); // UTF-8 디코딩
        List<dynamic> reviewList = jsonDecode(responseBody);
        if (reviewList.isNotEmpty) {
          for (var review in reviewList) {
            var instance = ReviewModel.fromJson(review);
            reviewInstances.add(instance);
          }
        }
        return reviewInstances;
      } else {
        print('ServerMessage: ${response.body}');
        throw Exception('Failed to load song: ${response.statusCode}');
      }
    } catch (e) {
      print(e);
      return reviewInstances;
    }
  }
}
