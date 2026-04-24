package com.firestreak.task_service.messaging;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskCompletedEvent(Long userId,
                                 Long taskId,
                                 LocalDateTime completedAt,
                                 Double count) {}
