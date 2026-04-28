package com.firestreak.streak_service.exception;

public class UserStreakNotFoundException extends RuntimeException {
    public UserStreakNotFoundException(Long userId) {
        super("streak of user "+userId+" not found");
    }
}
