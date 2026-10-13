package com.claudiumeiu.tidybalance_backend.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.claudiumeiu.tidybalance_backend.user.dto.CreateUserRequest;
import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUser;
import com.claudiumeiu.tidybalance_backend.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public CurrentUser me() {
        return userService.me();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody CreateUserRequest request) {

        userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
