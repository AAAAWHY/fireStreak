package com.firestreak.streak_service.exception;

public class TaskStreakNotFoundException extends RuntimeException {
    public TaskStreakNotFoundException(Long taskId) {
        super("streak of task "+taskId+" not found");
    }
}
