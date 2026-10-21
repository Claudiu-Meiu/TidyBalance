package com.claudiumeiu.tidybalance_backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(

                @NotBlank @Size(min = 4, max = 30) @Pattern(regexp = "^\\S+(?: \\S+)*$", message = "Nickname may contain only one space between words.") String nickname,

                @NotBlank @Size(min = 8, max = 30) @Pattern(regexp = "^\\S+$", message = "Username must not contain whitespaces.") String username,

                @NotBlank @Size(min = 8, max = 50) @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])\\S+$", message = "Password must contain an uppercase letter, a lowercase letter, a number, a special character, and no whitespace.") String password

) {
}