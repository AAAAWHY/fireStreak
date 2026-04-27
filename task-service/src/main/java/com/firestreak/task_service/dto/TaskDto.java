package com.firestreak.task_service.dto;

import com.firestreak.task_service.entity.TaskEntity;
import com.firestreak.task_service.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskDto(
        Long id,
        String title,
        String description,
        Integer frequencyDays,
        Long userId,
        Double targetCount,
        Double completedCount,
        LocalDateTime lastCompletedAt,
        TaskType taskType,
        Long folderId
) {
    public static TaskDto from(TaskEntity task){
        return TaskDto.builder()
                .id(task.getId())
                .completedCount(task.getCompletedCount())
                .taskType(task.getTaskType())
                .userId(task.getUserId())
                .description(task.getDescription())
                .targetCount(task.getTargetCount())
                .frequencyDays(task.getFrequencyDays())
                .lastCompletedAt(task.getLastCompletedAt())
                .folderId(task.getFolderId())
                .title(task.getTitle())
                .build();
    }
}
