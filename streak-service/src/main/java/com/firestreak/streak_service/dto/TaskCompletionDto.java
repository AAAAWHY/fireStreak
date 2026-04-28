package com.firestreak.streak_service.dto;

import com.firestreak.streak_service.entity.TaskCompletion;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskCompletionDto(Double completedCount,
                                Long taskId,
                                Long userId,
                                LocalDate completedAt)

{
    public static TaskCompletionDto from(TaskCompletion taskCompletion){
        return TaskCompletionDto.builder()
                .completedAt(taskCompletion.getCompletedAt())
                .taskId(taskCompletion.getTaskId())
                .userId(taskCompletion.getUserId())
                .completedCount(taskCompletion.getCompletedCount())
                .build();
    }
}
