package com.claudiumeiu.tidybalance_backend.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.claudiumeiu.tidybalance_backend.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByPublicUserId(UUID publicUserId);

    List<User> findByNicknameContainingIgnoreCase(String nickname);

    boolean existsByNickname(String nickname);

    boolean existsByUsername(String username);
}
