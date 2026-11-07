package com.claudiumeiu.tidybalance_backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeUserNicknameRequestDto(
        @NotBlank @Size(min = 4, max = 30) @Pattern(regexp = "^\\S+(?: \\S+)*$", message = "Nickname may contain only one space between words.") String newNickname) {

}
