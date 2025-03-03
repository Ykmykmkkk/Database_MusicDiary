package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByUsernameAndDeleted(String username, boolean deleted);
    Optional<UserEntity> findByUsernameAndDeleted(String username, boolean deleted);
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByIdAndDeleted(UUID userId, boolean deleted);
}
