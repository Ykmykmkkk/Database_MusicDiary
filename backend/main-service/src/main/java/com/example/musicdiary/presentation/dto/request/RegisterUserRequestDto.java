package com.example.musicdiary.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RegisterUserRequestDto {
    @NotNull // NotNull: 널 값 불가, NotEmpty: 널 및 "" 불가, NotBlank: 널, "", " " 불가
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;

    private String email;
}
