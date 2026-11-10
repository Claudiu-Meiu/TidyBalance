package com.claudiumeiu.tidybalance_backend.user.dto;

import java.time.Instant;
import java.util.UUID;

public record PublicUserProfileSearchResultDto(
        UUID publicUserId,
        String nickname,
        Instant accountCreatedAt) {

}
