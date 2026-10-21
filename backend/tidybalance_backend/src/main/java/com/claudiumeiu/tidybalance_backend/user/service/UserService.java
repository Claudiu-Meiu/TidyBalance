package com.claudiumeiu.tidybalance_backend.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.claudiumeiu.tidybalance_backend.user.dto.CreateUserRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUserAuthenticatedDto;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileDto;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileSearchResultDto;
import com.claudiumeiu.tidybalance_backend.user.entity.User;
import com.claudiumeiu.tidybalance_backend.user.exception.NicknameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.exception.PublicUserProfileNotFoundException;
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

    public CurrentUserAuthenticatedDto me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
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

        User user = new User();

        user.setPublicUserId(UUID.randomUUID());
        user.setNickname(request.nickname());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    public PublicUserProfileDto getPublicUserProfile(UUID publicUserId) {
        User user = userRepository.findByPublicUserId(publicUserId).orElseThrow(() -> new PublicUserProfileNotFoundException());

        return new PublicUserProfileDto(user.getPublicUserId(), user.getNickname());
    }

    public List<PublicUserProfileSearchResultDto> searchUsers(String query) {
        String normalizedQuery = query.trim();

        if (normalizedQuery.length() < 2) {
            return List.of();
        }

        return userRepository.findByNicknameContainingIgnoreCase(query)
                .stream()
                .map(user -> new PublicUserProfileSearchResultDto(
                        user.getPublicUserId(),
                        user.getNickname()))
                .toList();
    }
}
