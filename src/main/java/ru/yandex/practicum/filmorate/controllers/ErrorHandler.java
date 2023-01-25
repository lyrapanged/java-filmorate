package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundException(final NotFoundException e) {
        String result = e.getMessage();
        log.info("Entity doesn't exist {}", e.getMessage());
        return Map.of("Error", result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> ex(final MethodArgumentNotValidException e) {
        log.info("MethodArgumentNotValidException{}", e.getMessage());
        String result = e.getMessage();
        return Map.of("Error", result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> exe(final ValidationException e) {
        log.info("ValidationException {}", e.getMessage());
        String result = e.getMessage();
        return Map.of("Error", result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> rer(final ConstraintViolationException e) {
        log.info("ConstraintViolationException {}", e.getMessage());
        String result = e.getMessage();
        return Map.of("Error", result);
    }

    @ExceptionHandler
    public ResponseEntity<StackTraceElement[]> unhandledException(final Throwable e) {
        log.error("Unhandled exception{}", e.getMessage());
        return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
