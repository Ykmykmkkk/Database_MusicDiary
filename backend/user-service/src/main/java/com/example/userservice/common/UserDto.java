package com.example.userservice.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor // ModelMapper에서 필요

public class UserDto {
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String email;
    private boolean deleted;
}
