package com.example.userservice.presentation.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RegisterUserRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;

    private String email;
}
