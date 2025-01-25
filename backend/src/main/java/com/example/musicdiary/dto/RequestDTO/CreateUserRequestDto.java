package com.example.musicdiary.dto.RequestDTO;

import com.example.musicdiary.dto.UserDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class CreateUserRequestDto {
    @NotNull // NotNull: 널 값 불가, NotEmpty: 널 및 "" 불가, NotBlank: 널, "", " " 불가
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;

    private String email;
    public UserDto toServiceDto(String encodedPassword) {
        return UserDto.builder()
                .username(username)
                .name(name)
                .email(email)
                .password(encodedPassword)
                .build();
    }

}
