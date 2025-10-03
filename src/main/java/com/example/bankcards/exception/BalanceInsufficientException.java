package com.example.bankcards.exception;

public class BalanceInsufficientException extends RuntimeException {
    public BalanceInsufficientException(Long cardId) {
        super("There is not enough money on the card with the id " + cardId + " for the transfer");
    }
}
