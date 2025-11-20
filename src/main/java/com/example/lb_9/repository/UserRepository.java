package com.example.lb_9.repository;

import com.example.lb_9.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    Optional<Object> findByEmail(String email);
}