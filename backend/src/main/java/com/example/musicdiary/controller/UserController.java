package com.example.musicdiary.controller;

import com.example.musicdiary.dto.RequestDTO.CreateUserRequestDto;
import com.example.musicdiary.dto.RequestDTO.DuplicateUserRequestDto;
import com.example.musicdiary.dto.RequestDTO.LoginRequestDto;
import com.example.musicdiary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")

public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto createUserDto) {
        // try catch 문을 사용하지 않고 uncheked exception 처리.
        // 반드시 컴파일 단계에서 예외 처리할 이유나 해결할 로직이 없으니까 GlobalExveptionHandler를 통해 관리
        userService.createUser(createUserDto.toServiceDto(passwordEncoder.encode(createUserDto.getPassword())));
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            userService.login(loginRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body("login success");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/duplicate")
    public ResponseEntity<?> checkDuplicate(@RequestBody DuplicateUserRequestDto duplicateRequestDto) {
        try {
            userService.checkDuplicate(duplicateRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body("Duplicate check success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Duplicate check failed");
        }
    }

    @PatchMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        try {
            userService.deleteUser(username);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Duplicate check failed");
        }
    }
}
