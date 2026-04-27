package com.firestreak.streak_service.messaging;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record TaskCompletedEvent(Long userId,
                                 Long taskId,
                                 LocalDate completedAt,
                                 Double count,
                                 Integer frequencyDays) {}