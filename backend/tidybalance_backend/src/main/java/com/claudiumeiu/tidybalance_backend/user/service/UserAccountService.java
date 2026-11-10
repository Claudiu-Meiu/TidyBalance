package com.claudiumeiu.tidybalance_backend.user.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUserAuthenticatedDto;
import com.claudiumeiu.tidybalance_backend.user.dto.ChangeUserNicknameRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.ChangeUserPasswordRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.ChangeUserUsernameRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.CreateUserRequestDto;
import com.claudiumeiu.tidybalance_backend.user.entity.UserEntity;
import com.claudiumeiu.tidybalance_backend.user.exception.IncorrectPasswordException;
import com.claudiumeiu.tidybalance_backend.user.exception.NewPasswordMustBeDifferentException;
import com.claudiumeiu.tidybalance_backend.user.exception.RetypedNewPasswordDoesntMatchException;
import com.claudiumeiu.tidybalance_backend.user.exception.NicknameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.exception.UsernameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setAccountCreatedAt(Instant.now());

        userRepository.saveAndFlush(user);
    }

    private UserEntity currentUser() {
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

    public void changePassword(ChangeUserPasswordRequestDto request) {
        UserEntity user = currentUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new NewPasswordMustBeDifferentException();
        }
        if (!request.newPassword().equals(request.retypedNewPassword())) {
            throw new RetypedNewPasswordDoesntMatchException();
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.saveAndFlush(user);
    }

    public CurrentUserAuthenticatedDto changeNickname(ChangeUserNicknameRequestDto request) {
        UserEntity user = currentUser();

        if (userRepository.existsByNicknameAndIdNot(request.newNickname(), user.getId())) {
            throw new NicknameAlreadyExistsException();
        }

        user.setNickname(request.newNickname());

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new NicknameAlreadyExistsException();
        }

        return new CurrentUserAuthenticatedDto(
                user.getPublicUserId(),
                user.getNickname(),
                user.getUsername(),
                user.getAccountCreatedAt());
    }

    public void changeUsername(ChangeUserUsernameRequestDto request) {
        UserEntity user = currentUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        if (userRepository.existsByUsernameAndIdNot(request.newUsername(), user.getId())) {
            throw new UsernameAlreadyExistsException();
        }

        user.setUsername(request.newUsername());

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException();
        }
    }

    public void deleteUser() {
        userRepository.delete(currentUser());
    }
}