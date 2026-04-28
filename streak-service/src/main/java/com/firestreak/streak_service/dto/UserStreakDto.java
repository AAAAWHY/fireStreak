package com.firestreak.streak_service.dto;

import com.firestreak.streak_service.entity.UserStreak;
import lombok.Builder;

@Builder
public record UserStreakDto(Long userId,
                            Integer streakCount,
                            Integer hardStreakCount)

{
    public static UserStreakDto from(UserStreak userStreak){
        return UserStreakDto.builder()
                .userId(userStreak.getUserId())
                .streakCount(userStreak.getStreakCount())
                .hardStreakCount(userStreak.getHardStreakCount())
                .build();
    }
}
