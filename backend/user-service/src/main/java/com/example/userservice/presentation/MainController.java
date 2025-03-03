package com.example.userservice.presentation;

import com.example.userservice.application.UserService;
import com.example.userservice.common.UserDto;
import com.example.userservice.presentation.dto.requestDto.DuplicateUserRequestDto;
import com.example.userservice.presentation.dto.requestDto.RegisterUserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class MainController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        // try catch 문을 사용하지 않고 uncheked exception 처리.
        // 반드시 컴파일 단계에서 예외 처리할 이유나 해결할 로직이 없으니까 GlobalExveptionHandler를 통해 관리
        UserDto registerUserDto = UserDto.builder()
                .email(registerUserRequestDto.getEmail())
                .name(registerUserRequestDto.getName())
                .username(registerUserRequestDto.getUsername())
                .password(registerUserRequestDto.getPassword()).build();
        userService.registerUser(registerUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("UserEntity created successfully");
    }
    @PostMapping("/duplicate")
    public ResponseEntity<?> checkDuplicate(@RequestBody DuplicateUserRequestDto duplicateRequestDto) {
            UserDto duplicateUserDto =  UserDto.builder()
                    .username(duplicateRequestDto.getUsername()).build();
            userService.checkDuplicate(duplicateUserDto);
            return ResponseEntity.status(HttpStatus.OK).body("Duplicate check success");
    }

    @PatchMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
            userService.deleteUser(username);
            return ResponseEntity.status(HttpStatus.OK).body("UserEntity deleted success");
    }
}
