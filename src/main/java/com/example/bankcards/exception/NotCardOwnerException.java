package com.example.bankcards.exception;

public class NotCardOwnerException extends RuntimeException {
    public NotCardOwnerException(long userId, long cardId) {
        super("User with id" + userId + " is not the owner of the card with id " + cardId);
    }
}
