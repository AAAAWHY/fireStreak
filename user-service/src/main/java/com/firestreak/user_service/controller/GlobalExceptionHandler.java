package com.firestreak.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> globalHandler(Exception e){
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> responseStatusHandler(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
    }
}