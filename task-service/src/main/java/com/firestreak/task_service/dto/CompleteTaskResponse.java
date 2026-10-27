package com.firestreak.task_service.dto;


import jakarta.validation.constraints.NotNull;

public record CompleteTaskResponse(
        TaskDto taskDto,
        Boolean needMessage,
        VerdictDto verdictDto
) {
}
