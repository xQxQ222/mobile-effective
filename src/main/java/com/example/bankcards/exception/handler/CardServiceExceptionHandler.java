package com.example.bankcards.exception.handler;

import com.example.bankcards.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CardServiceExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(final NotFoundException e) {
        return new ExceptionResponse("Object was not found", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalCardStatusException(final IllegalCardStateException e) {
        return new ExceptionResponse("Illegal card state", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalRequestStateException(final IllegalRequestStateException e) {
        return new ExceptionResponse("Illegal request state", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleAlreadyExistsException(final AlreadyExistsException e) {
        return new ExceptionResponse("Object already exists", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleNotCardOwnerException(final NotCardOwnerException e) {
        return new ExceptionResponse("User is not card owner", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleNotRequestInitiatorException(final NotRequestInitiatorException e) {
        return new ExceptionResponse("User is not request initiator", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBalanceInsufficientException(final BalanceInsufficientException e) {
        return new ExceptionResponse("Balance is insufficient", e.getMessage());
    }
}
