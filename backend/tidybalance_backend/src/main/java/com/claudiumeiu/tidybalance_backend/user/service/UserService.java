package com.claudiumeiu.tidybalance_backend.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUser;
import com.claudiumeiu.tidybalance_backend.user.entity.User;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CurrentUser me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + auth.getName()));

        return new CurrentUser(
                user.getDisplayedName());
    }
}
