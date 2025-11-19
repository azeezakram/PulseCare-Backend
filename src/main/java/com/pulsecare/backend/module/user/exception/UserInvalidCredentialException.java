package com.pulsecare.backend.module.user.exception;

public class UserInvalidCredentialException extends RuntimeException {
    public UserInvalidCredentialException(String message) {
        super(message);
    }
}
