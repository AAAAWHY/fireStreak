package com.firestreak.task_service.dto;

import com.firestreak.task_service.enums.TaskType;

import java.time.LocalDateTime;

public record TaskDto(
        Long id,
        String title,
        String description,
        Long frequencyMinutes,
        Long userId,
        Double targetCount,
        Double completedCount,
        LocalDateTime lastCompletedAt,
        TaskType taskType,
        Long folderId
) {
}
