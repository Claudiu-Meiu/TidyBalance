package com.claudiumeiu.tidybalance_backend.user.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUserAuthenticatedDto;
import com.claudiumeiu.tidybalance_backend.user.entity.UserEntity;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class UserAuthService {

    private final UserRepository userRepository;

    public UserAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity currentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public CurrentUserAuthenticatedDto me() {
        UserEntity user = currentUser();

        return new CurrentUserAuthenticatedDto(
                user.getPublicUserId(),
                user.getNickname(),
                user.getUsername(),
                user.getAccountCreatedAt());
    }
}
