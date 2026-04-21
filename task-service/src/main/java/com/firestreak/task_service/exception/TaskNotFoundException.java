package com.firestreak.task_service.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("task " + taskId + " not found");
    }
}
