package com.firestreak.ai_service;

public record AskModelDto(
        Long userId,
        Long taskId,
        String message,
        String taskTitle,
        String taskDescription,
        Double targetCount
) {
}
