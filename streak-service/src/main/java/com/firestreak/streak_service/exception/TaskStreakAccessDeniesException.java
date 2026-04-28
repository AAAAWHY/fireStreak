package com.firestreak.streak_service.exception;

public class TaskStreakAccessDeniesException extends RuntimeException {
    public TaskStreakAccessDeniesException(Long taskId) {
        super("task "+ taskId + " is not yours");
    }
}
