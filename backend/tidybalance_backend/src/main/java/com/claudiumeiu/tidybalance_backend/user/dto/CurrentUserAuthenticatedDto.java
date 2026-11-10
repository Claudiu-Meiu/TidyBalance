package com.claudiumeiu.tidybalance_backend.user.dto;

import java.time.Instant;
import java.util.UUID;

public record CurrentUserAuthenticatedDto(
        UUID publicUserId,
        String nickname,
        String username,
        Instant accountCreatedAt) {
}
