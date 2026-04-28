package com.firestreak.streak_service.controller;

import com.firestreak.streak_service.exception.TaskStreakAccessDeniesException;
import com.firestreak.streak_service.exception.TaskStreakNotFoundException;
import com.firestreak.streak_service.exception.UserStreakNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskStreakAccessDeniesException.class)
    ResponseEntity<String> TaskStreakAccessDeniesExceptionHandler(TaskStreakAccessDeniesException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(TaskStreakNotFoundException.class)
    ResponseEntity<String> TaskStreakNotFoundExceptionHandler(TaskStreakNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserStreakNotFoundException.class)
    ResponseEntity<String> UserStreakNotFoundExceptionHandler(UserStreakNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> globalHandler(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
