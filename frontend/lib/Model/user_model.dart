class UserModel {
  final String username;
  final String password;
  final String name;
  final String email;
  UserModel.fromJson(Map<String, dynamic> json)
      : username = json['username'],
        password = json['password'],
        name = json['name'],
        email = json['email'];
}
