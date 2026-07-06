package com.firestreak.flame_service.controller;

import com.firestreak.flame_service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CatNotFoundException.class)
    ResponseEntity<String> catNotFoundHandler(CatNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MaxLootBoxGradeException.class)
    ResponseEntity<String> maxLootBoxGradeHandler(MaxLootBoxGradeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotEnoughFlamesException.class)
    ResponseEntity<String> notEnoughFlamesHandler(NotEnoughFlamesException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotEnoughPurrsException.class)
    ResponseEntity<String> notEnoughPurrsHandler(NotEnoughPurrsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotYourCatException.class)
    ResponseEntity<String> notYourCatHandler(NotYourCatException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
