package com.epam.hibernate.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}