package com.example.bankcards.exception;

public class IllegalCardStateException extends RuntimeException {
    public IllegalCardStateException(String message) {
        super(message);
    }
}
