package com.claudiumeiu.tidybalance_backend.user.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.claudiumeiu.tidybalance_backend.user.dto.CreateUserRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUserAuthenticatedDto;
import com.claudiumeiu.tidybalance_backend.user.entity.UserEntity;
import com.claudiumeiu.tidybalance_backend.user.exception.NicknameAlreadyExistsException;

import com.claudiumeiu.tidybalance_backend.user.exception.UsernameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CurrentUserAuthenticatedDto me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));

        return new CurrentUserAuthenticatedDto(
                user.getPublicUserId(),
                user.getNickname(),
                user.getUsername());
    }

    public void createUser(CreateUserRequestDto request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new NicknameAlreadyExistsException();
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException();
        }

        UserEntity user = new UserEntity();

        user.setPublicUserId(UUID.randomUUID());
        user.setNickname(request.nickname());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

}
