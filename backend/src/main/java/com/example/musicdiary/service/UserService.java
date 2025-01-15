package com.example.musicdiary.service;

import com.example.musicdiary.domain.User;
import com.example.musicdiary.dto.RequestDTO.CreateUserRequestDto;
import com.example.musicdiary.dto.RequestDTO.DuplicateUserRequestDto;
import com.example.musicdiary.dto.RequestDTO.LoginRequestDto;
import com.example.musicdiary.dto.UserDto;
import com.example.musicdiary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createUser(UserDto userDto) {
        isDuplicated(userDto.getUsername());
        userRepository.save(userDto.toEntity());
    }
    @Transactional
    public void login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String rawPassword = loginRequestDto.getPassword();
        User user = userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다."));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    public void checkDuplicate(DuplicateUserRequestDto duplicateUserRequestDto) {
        String username = duplicateUserRequestDto.getUsername();
        isDuplicated(username);
    }

    public void securityLogin(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void isDuplicated(String username) {
        if (userRepository.existsByUsernameAndDeleted(username, false)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }
    @Transactional(readOnly = true)
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(String username) {
        User user = userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        user.delete();
        userRepository.save(user);
    }
}

