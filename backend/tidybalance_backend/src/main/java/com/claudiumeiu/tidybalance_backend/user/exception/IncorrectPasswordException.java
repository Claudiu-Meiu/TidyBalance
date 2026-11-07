package com.claudiumeiu.tidybalance_backend.user.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Password is incorrect.");
    }

}
