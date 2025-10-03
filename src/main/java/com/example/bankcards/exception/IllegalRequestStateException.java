package com.example.bankcards.exception;

public class IllegalRequestStateException extends RuntimeException {
    public IllegalRequestStateException(String message) {
        super(message);
    }
}
