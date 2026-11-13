package com.claudiumeiu.tidybalance_backend.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUserAuthenticatedDto;
import com.claudiumeiu.tidybalance_backend.user.service.UserAuthService;

@RestController
@RequestMapping("/api/users/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping("/me")
    public CurrentUserAuthenticatedDto me() {
        return userAuthService.me();
    }
}
