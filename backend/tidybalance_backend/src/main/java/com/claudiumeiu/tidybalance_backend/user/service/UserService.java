package com.claudiumeiu.tidybalance_backend.user.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.claudiumeiu.tidybalance_backend.user.dto.CreateUserRequest;
import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUser;
import com.claudiumeiu.tidybalance_backend.user.entity.User;
import com.claudiumeiu.tidybalance_backend.user.exception.NicknameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.exception.UsernameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CurrentUser me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + username));

        return new CurrentUser(
                user.getNickname(),
                user.getUsername());
    }

    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new NicknameAlreadyExistsException();
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User();

        user.setNickname(request.nickname());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }
}
