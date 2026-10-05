package com.claudiumeiu.tidybalance_backend.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUser;
import com.claudiumeiu.tidybalance_backend.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public CurrentUser me() {
        return userService.me();
    }
}
