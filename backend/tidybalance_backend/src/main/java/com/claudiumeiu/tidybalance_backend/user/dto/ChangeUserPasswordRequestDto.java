package com.claudiumeiu.tidybalance_backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeUserPasswordRequestDto(
        @NotBlank(message = "Current password is required.") String currentPassword,

        @NotBlank(message = "New password is required.") @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])\\S+$", message = "Password must contain an uppercase letter, a lowercase letter, a number, a special character, and no whitespace.") String newPassword,

        @NotBlank(message = "Retyped new password is required.") String retypedNewPassword) {
}