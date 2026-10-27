package com.firestreak.task_service.dto;

public record AskModelDto(
        Long userId,
        Long taskId,
        String message,
        String taskTitle,
        String taskDescription,
        Double targetCount
) {
}
