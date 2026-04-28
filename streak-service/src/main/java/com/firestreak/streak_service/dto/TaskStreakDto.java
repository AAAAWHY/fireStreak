package com.firestreak.streak_service.dto;

import com.firestreak.streak_service.entity.TaskStreak;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskStreakDto(LocalDate lastCompletedAt,
                            Long taskId,
                            Long userId,
                            Integer streakCount,
                            Integer freezeStreak,
                            Integer frequencyDays)

{
    public static TaskStreakDto from(TaskStreak taskStreak){
        return TaskStreakDto.builder()
                .lastCompletedAt(taskStreak.getLastCompletedAt())
                .taskId(taskStreak.getTaskId())
                .userId(taskStreak.getUserId())
                .streakCount(taskStreak.getStreakCount())
                .freezeStreak(taskStreak.getFreezeStreakCount())
                .frequencyDays(taskStreak.getFrequencyDays())
                .build();
    }
}
