package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleValidationException(final ValidationException e) {
        log.error("Invalid validation");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> exc(final ConstraintViolationException e) {
        log.error("Incorrect variable count");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> notFoundException(final NotFoundException e) {
        log.error("Entity doesn't exist");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> ex(final MethodArgumentNotValidException e) {
        log.error("Unhandled exception");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> unhandledException(final Throwable e) {
        log.error("Unhandled exception");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
