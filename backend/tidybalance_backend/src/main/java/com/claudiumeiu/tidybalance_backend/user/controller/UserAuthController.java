package com.claudiumeiu.tidybalance_backend.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.claudiumeiu.tidybalance_backend.user.dto.CreateUserRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUserAuthenticatedDto;
import com.claudiumeiu.tidybalance_backend.user.service.UserAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserAuthController {

    private final UserAuthService userService;

    public UserAuthController(UserAuthService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public CurrentUserAuthenticatedDto me() {
        return userService.me();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody CreateUserRequestDto request) {

        userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
