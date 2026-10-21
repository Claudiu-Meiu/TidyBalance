package com.claudiumeiu.tidybalance_backend.user.dto;

import java.util.UUID;

public record PublicUserProfileDto(UUID publicUserId, String nickname) {

}
