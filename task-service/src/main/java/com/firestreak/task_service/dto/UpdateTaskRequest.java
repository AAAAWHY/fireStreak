package com.firestreak.task_service.dto;

import com.firestreak.task_service.enums.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(String title,
                                TaskType taskType,
                                String description,
                                Long frequencyMinutes,
                                Double targetCount,
                                Long folderId) {}
