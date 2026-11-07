package com.claudiumeiu.tidybalance_backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeUserUsernameRequestDto(
        @NotBlank(message = "New username is required.") @Size(min = 8, max = 30, message = "Username must be between 8 and 30 characters.") @Pattern(regexp = "^\\S+$", message = "Username must not contain whitespaces.") String newUsername,

        @NotBlank(message = "Current password is required.") String currentPassword) {
}