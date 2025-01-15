import 'dart:convert';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;

class AuthService {
  static final hostAddress = dotenv.env['API_ADDRESS'];

  static Future<bool> duplicate(String username) async {
    var duplicateCheck = false;
    var headers = {'Content-Type': 'application/json'};
    var request = http.Request(
        'POST', Uri.parse('http://$hostAddress:8080/user/duplicate'));
    request.body = jsonEncode({"username": username});
    request.headers.addAll(headers);
    http.StreamedResponse response = await request.send();
    if (response.statusCode == 200) {
      duplicateCheck = true;
    }
    return duplicateCheck;
  }

  static Future<bool> register({
    required String username,
    required String password,
    required String name,
    required String email,
  }) async {
    var headers = {'Content-Type': 'application/json'};
    var request =
        http.Request('POST', Uri.parse('http://$hostAddress:8080/user/create'));
    request.body = jsonEncode({
      "username": username,
      "password": password,
      "name": name,
      "email": email,
    });
    request.headers.addAll(headers);
    http.StreamedResponse response = await request.send();
    print('회원가입API:${response.statusCode}');
    if (response.statusCode == 201) {
      return true;
    } else {
      return false;
    }
  }

  static Future<bool> login(
      {required String username, required String password}) async {
    var headers = {'Content-Type': 'application/json'};
    var request =
        http.Request('POST', Uri.parse('http://$hostAddress:8080/user/login'));
    request.body = json.encode({
      "username": username,
      "password": password,
    });
    request.headers.addAll(headers);
    var client = http.Client();
    try {
      // 요청을 보내고 타임아웃을 설정
      http.StreamedResponse response = await client
          .send(request)
          .timeout(const Duration(seconds: 20), onTimeout: () {
        // 타임아웃 발생 시
        client.close(); // 클라이언트 닫기
        throw Exception('Request timeout');
      });
      print("내가왔다");
      print(username);
      print(password);

      if (response.statusCode == 200) {
        return true;
      } else if (response.statusCode == 400) {
        return false;
      } else {
        print(response.statusCode); // 기타 로그인 Service error
        return false;
      }
    } catch (e) {
      //  request Timeout시
      print('Error: $e');
      return false;
    } finally {
      client.close(); // 클라이언트 닫기
    }
  }

  static Future<String> findUserName(
      {required String name,
      required String email,
      required String phone}) async {
    var headers = {'Content-Type': 'application/json'};
    var request = http.Request(
        'POST', Uri.parse('http://$hostAddress:8080/user/find-username'));
    request.body = json.encode({
      "name": name,
      "email": email,
      "phone": phone,
    });
    request.headers.addAll(headers);
    try {
      http.StreamedResponse response = await request.send();
      String responseBody = await response.stream.bytesToString();
      if (response.statusCode == 200) {
        var decodedResponse = json.decode(responseBody);
        if (decodedResponse != null &&
            decodedResponse.containsKey('username')) {
          return decodedResponse['username'];
        } else {
          print('Username not found in response');
          return '';
        }
      } else if (response.statusCode == 400) {
        print("400에러 $responseBody");
        return '';
      } else {
        print(response.statusCode); // 기타 로그인 Service error
        return '';
      }
    } catch (e) {
      //  request Timeout시
      print('Error: $e');
      return '';
    }
  }

  static Future<bool> resetPassword(
      {required String username,
      required String name,
      required String email,
      required String phone,
      required String newPassword}) async {
    var headers = {'Content-Type': 'application/json'};
    var request = http.Request(
        'POST', Uri.parse('http://$hostAddress:8080/user/reset-password'));
    request.body = json.encode({
      "username": username,
      "name": name,
      "email": email,
      "phone": phone,
      "newPassword": newPassword,
    });
    request.headers.addAll(headers);
    try {
      http.StreamedResponse response = await request.send();
      String responseBody = await response.stream.bytesToString();
      if (response.statusCode == 200) {
        return true;
      } else {
        print("${response.statusCode}에러 $responseBody");
        return false;
      }
    } catch (e) {
      //  request Timeout시
      print('Error: $e');
      rethrow;
    }
  }

  static Future<bool> memberModify({
    // 나중에 수정하기
    required String name,
    required String email,
    required String phone,
    required String password,
  }) async {
    var headers = {'Content-Type': 'application/json', 'username': name};
    var request =
        http.Request('PATCH', Uri.parse('http://$hostAddress:8080/user/'));
    request.body = jsonEncode({
      "name": name,
      "email": email,
      "phone": phone,
      "password": password,
    });
    request.headers.addAll(headers);
    http.StreamedResponse response = await request.send();
    if (response.statusCode == 200) {
      return true;
    } else {
      print(response.statusCode);
      return false;
    }
  }

  static Future<void> delete(String username) async {
    final headers = {'Content-Type': 'application/json'};
    final url = Uri.parse('http://$hostAddress:8080/user/delete');
    final body = jsonEncode({'username': username});

    try {
      final response = await http.patch(
        url,
        headers: headers,
        body: body,
      );

      if (response.statusCode == 200) {
        print("삭제 성공");
        return;
      } else {
        final responseBody = response.body;
        print("삭제 실패");
        print('ServerMessage: $responseBody');
        throw Exception("Failed to delete user: ${response.statusCode}");
      }
    } catch (e) {
      print("예외 발생: $e");
      throw Exception("An error occurred while deleting the user.");
    }
  }
}
