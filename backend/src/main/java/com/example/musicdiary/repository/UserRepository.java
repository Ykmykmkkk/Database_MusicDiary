package com.example.musicdiary.repository;

import com.example.musicdiary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameAndDeleted(String username, boolean deleted);
    Optional<User> findByUsernameAndDeleted(String username, boolean deleted);
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPasswordAndDeleted(String username, String password, boolean deleted);
}
