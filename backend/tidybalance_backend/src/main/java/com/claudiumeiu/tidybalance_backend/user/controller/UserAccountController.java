package com.claudiumeiu.tidybalance_backend.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.claudiumeiu.tidybalance_backend.user.dto.ChangeUserNicknameRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.ChangeUserPasswordRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.ChangeUserUsernameRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.CreateUserRequestDto;
import com.claudiumeiu.tidybalance_backend.user.dto.CurrentUserAuthenticatedDto;
import com.claudiumeiu.tidybalance_backend.user.service.UserAccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @Valid @RequestBody CreateUserRequestDto request) {

        userAccountService.createUser(request);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangeUserPasswordRequestDto request) {
        userAccountService.changePassword(request);
    }

    @PatchMapping("/nickname")
    public CurrentUserAuthenticatedDto changeNickname(@Valid @RequestBody ChangeUserNicknameRequestDto request) {
        return userAccountService.changeNickname(request);
    }

    @PatchMapping("/username")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeUsername(@Valid @RequestBody ChangeUserUsernameRequestDto request) {
        userAccountService.changeUsername(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount() {
        userAccountService.deleteUser();
    }
}
