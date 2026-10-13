package com.claudiumeiu.tidybalance_backend.user.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException() {
        super("Username is already taken.");
    }
}