package com.claudiumeiu.tidybalance_backend.user.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteUserAccountRequestDto(
        @NotBlank(message = "Current password is required.") String currentPassword) {

}
