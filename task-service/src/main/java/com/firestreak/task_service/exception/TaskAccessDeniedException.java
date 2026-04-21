package com.firestreak.task_service.exception;

public class TaskAccessDeniedException extends RuntimeException {
    public TaskAccessDeniedException(Long taskId) {
        super("Task " + taskId + " is not yours");
    }
}
