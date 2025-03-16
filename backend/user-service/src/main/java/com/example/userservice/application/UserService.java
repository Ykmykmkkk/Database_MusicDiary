package com.example.userservice.application;

import com.example.userservice.common.UserDto;
import com.example.userservice.domain.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.CustomUserDetails;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ValidationService validationService;
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("해당 회원이 존재하지 않습니다."));
        return new CustomUserDetails(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                Collections.emptyList(),
                //userEntity.getAuthorities(),
                true,true,true,true);
    }

    @Transactional
    public void registerUser(UserDto userDto) {
        try {
            String encodedPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

            UserEntity userEntity = UserEntity.builder()
                    .id(UUID.randomUUID())
                    .username(userDto.getUsername())
                    .password(encodedPassword)
                    .email(userDto.getEmail())
                    .name(userDto.getName())
                    .build();

            validationService.checkValid(userEntity);
            userRepository.save(userEntity);
        } catch (ConstraintViolationException ex) {
            System.out.println("ConstraintViolationException caught in UserService!"); // 디버깅 로그
            throw ex; // 예외를 다시 던져 GlobalExceptionHandler로 전파
        }
    }

    public void checkDuplicate(UserDto duplicateUserDto) {
        String username = duplicateUserDto.getUsername();
        if (userRepository.existsByUsernameAndDeleted(username, false)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }
    @Transactional(readOnly = true)
    public UserDto getUserDtoByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return UserDto.builder().id(userEntity.getId()).username(userEntity.getUsername()).build();

    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(String username) {
        UserEntity userEntity = userRepository.findByUsernameAndDeleted(username, false)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        userEntity.delete();
        userRepository.save(userEntity);
    }


    public String getUsernameByUserId(UUID userId) {
        UserEntity userEntity = userRepository.findByIdAndDeleted(userId, false)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return userEntity.getUsername();
    }
}
