package com.claudiumeiu.tidybalance_backend.user.exception;

public class PublicUserProfileNotFoundException extends RuntimeException {

    public PublicUserProfileNotFoundException() {
        super("Public user profile not found.");
    }

}
