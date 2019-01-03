package com.turchenkov.parsing.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ThereIsSuchUserExceptionHandler {

    @ExceptionHandler(ThereIsSuchUserException.class)
    protected ResponseEntity<ThereIsSuchUserExceptionHandler.AwesomeException> handleThereIsNoSuchUserException() {
        return new ResponseEntity<>(new ThereIsSuchUserExceptionHandler.AwesomeException("There is no such user"), HttpStatus.CONFLICT);
    }

    @Data
    @AllArgsConstructor
    private static class AwesomeException {
        private String message;
    }
}
