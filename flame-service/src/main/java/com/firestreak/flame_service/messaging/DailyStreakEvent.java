package com.firestreak.flame_service.messaging;

public record DailyStreakEvent(Long userId, int streakCount) {
}
