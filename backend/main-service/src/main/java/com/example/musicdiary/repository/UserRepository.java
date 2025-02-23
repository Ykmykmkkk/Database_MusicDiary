package com.example.musicdiary.repository;

import com.example.musicdiary.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsernameAndDeleted(String username, boolean deleted);
    Optional<UserEntity> findByUsernameAndDeleted(String username, boolean deleted);
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndPasswordAndDeleted(String username, String password, boolean deleted);
}
