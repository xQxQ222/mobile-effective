package com.example.bankcards.exception.handler;

public class ExceptionResponse {
    private final String error;
    private final String description;

    public ExceptionResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
