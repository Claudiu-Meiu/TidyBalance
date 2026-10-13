package com.claudiumeiu.tidybalance_backend.user.exception;

public class NicknameAlreadyExistsException extends RuntimeException {

    public NicknameAlreadyExistsException() {
        super("Nickname is already taken.");
    }
}
