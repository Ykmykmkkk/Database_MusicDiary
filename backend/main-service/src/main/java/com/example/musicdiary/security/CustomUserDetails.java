package com.example.musicdiary.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data // UserDetails 메서드 오버라이드 (Lombok @Data가 자동 생성)
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id; // userId 추가
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    // 생성자: 기본값으로 초기화
    public CustomUserDetails(Long id, String username, String password,
                             Collection<? extends GrantedAuthority> authorities, boolean accountNonExpired,
                             boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

}
