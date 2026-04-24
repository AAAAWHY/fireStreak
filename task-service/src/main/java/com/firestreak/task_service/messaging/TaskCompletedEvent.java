package com.firestreak.task_service.messaging;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskCompletedEvent(Long userId,
                                 Long taskId,
                                 LocalDate completedAt,
                                 Double count) {}
