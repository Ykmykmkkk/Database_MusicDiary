package com.example.userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    UUID id;

    @Column(unique = true, nullable = false, length = 30)
    @Size(min=1, max=10)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false) // JPA 영속성과 관련된 annotation으로 데이터베이스 테이블에서 not null 옵션 설정
    @Size(min=1, max=10) // Validation annotation으로 서비스 레이어에서 도메인 객체에 대한 유효성 검사를 진행할 때 쓰임
    private String name;

    @Column(nullable = false)
    @NotNull
    private String email;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(name = "user_role")
    private String userRole;

    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole));
    }
    public void delete() {
        this.deleted = true;
    }

}
