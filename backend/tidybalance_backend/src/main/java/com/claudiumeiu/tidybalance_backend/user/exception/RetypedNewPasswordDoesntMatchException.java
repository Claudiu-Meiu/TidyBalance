package com.claudiumeiu.tidybalance_backend.user.exception;

public class RetypedNewPasswordDoesntMatchException extends RuntimeException {

    public RetypedNewPasswordDoesntMatchException() {
        super("Retyped new password doesn't match.");
    }
}
