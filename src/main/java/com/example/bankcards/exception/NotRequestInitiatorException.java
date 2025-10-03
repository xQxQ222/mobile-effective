package com.example.bankcards.exception;

public class NotRequestInitiatorException extends RuntimeException {
    public NotRequestInitiatorException(Long userId, Long requestId) {
        super("User with id" + userId + " not an initiator of request with id " + requestId);
    }
}
