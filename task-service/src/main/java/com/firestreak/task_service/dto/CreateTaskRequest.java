package com.firestreak.task_service.dto;

import com.firestreak.task_service.enums.TaskType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
        @NotBlank(message = "title can't be blank")
        String title,
        @NotNull(message = "taskType can't be null")
        TaskType taskType,
        String description,
        @NotNull(message = "frequency can't be null")
        Long frequencyMinutes,
        Double targetCount,
        Long folderId
) {}
