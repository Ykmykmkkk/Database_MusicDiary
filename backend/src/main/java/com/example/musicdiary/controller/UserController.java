package com.example.musicdiary.controller;

import com.example.musicdiary.dto.RequestDTO.CreateUserRequestDto;
import com.example.musicdiary.dto.RequestDTO.DuplicateUserRequestDto;
import com.example.musicdiary.dto.RequestDTO.LoginRequestDto;
import com.example.musicdiary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto createUserDto) {
        try {
            userService.createUser(createUserDto.toServiceDto(passwordEncoder.encode(createUserDto.getPassword())));
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

}
