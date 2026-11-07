package com.claudiumeiu.tidybalance_backend.user.exception;

public class NewPasswordMustBeDifferentException extends RuntimeException {

    public NewPasswordMustBeDifferentException() {
        super("New password must be different from the current password.");
    }
}
