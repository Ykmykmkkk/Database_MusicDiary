package com.example.userservice.presentation.dto.requestDto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
