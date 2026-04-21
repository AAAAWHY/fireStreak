package com.firestreak.task_service.controller;


import com.firestreak.task_service.exception.TaskAccessDeniedException;
import com.firestreak.task_service.exception.TaskNotFoundException;
import com.firestreak.task_service.exception.TaskValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskAccessDeniedException.class)
    ResponseEntity<String> taskAccessDeniedHandler(TaskAccessDeniedException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }


    @ExceptionHandler(TaskNotFoundException.class)
    ResponseEntity<String> taskNotFoundHandler(TaskNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }



    @ExceptionHandler(TaskValidationException.class)
    ResponseEntity<String> taskValidationHandler(TaskValidationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> methodArgumentNotValidHandler(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler()
    ResponseEntity<String> globalHandler(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}