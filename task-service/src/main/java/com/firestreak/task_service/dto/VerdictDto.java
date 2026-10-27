package com.firestreak.task_service.dto;

import com.firestreak.task_service.enums.Status;

public record VerdictDto(
        Status status,
        String responseMessage
) {
}
