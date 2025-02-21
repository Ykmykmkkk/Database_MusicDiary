package com.example.musicdiary.service;

import com.example.musicdiary.entity.UserEntity;
import com.example.musicdiary.presentation.dto.request.DuplicateUserRequestDto;
import com.example.musicdiary.presentation.dto.request.LoginRequestDto;
import com.example.musicdiary.presentation.dto.UserDto;
import com.example.musicdiary.repository.UserRepository;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow();
        return new User(userEntity.getUsername(),userEntity.getPassword(),true,true,
                true,true,new ArrayList<>());
    }
    @Transactional
    public void createUser(UserDto userDto) {
        try {
            UserEntity userEntity = userDto.toEntity();
            validationService.checkValid(userEntity);
            userRepository.save(userEntity);
        } catch (ConstraintViolationException ex) {
            System.out.println("ConstraintViolationException caught in UserService!"); // 디버깅 로그
            throw ex; // 예외를 다시 던져 GlobalExceptionHandler로 전파
        }
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String rawPassword = loginRequestDto.getPassword();
        UserEntity userEntity = userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다."));
        if (!passwordEncoder.matches(rawPassword, userEntity.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    public void checkDuplicate(DuplicateUserRequestDto duplicateUserRequestDto) {
        String username = duplicateUserRequestDto.getUsername();
        isDuplicated(username);
    }


    private void isDuplicated(String username) {
        if (userRepository.existsByUsernameAndDeleted(username, false)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }
    @Transactional(readOnly = true)
    public UserEntity getUserEntityByUsername(String username) {
        return userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(String username) {
        UserEntity userEntity = userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        userEntity.delete();
        userRepository.save(userEntity);
    }


}

