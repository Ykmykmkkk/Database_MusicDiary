package com.example.musicdiary.dto.RequestDTO;

import com.example.musicdiary.dto.UserDto;
import lombok.Data;

import java.util.List;


@Data
public class CreateUserRequestDto {
    private String username;

    private String password;

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
