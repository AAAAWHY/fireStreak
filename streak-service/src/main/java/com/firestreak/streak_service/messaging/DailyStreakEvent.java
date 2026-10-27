package com.firestreak.streak_service.messaging;

public record DailyStreakEvent(Long userId, int streakCount) { }