class UserModel {
  final String userId;
  final String username;
  final String password;
  final String name;
  final String email;
  UserModel.fromJson(Map<String, dynamic> json)
      : userId = json['id'],
        username = json['username'],
        password = json['password'],
        name = json['name'],
        email = json['email'];
}
