package com.firestreak.task_service.dto;

import com.firestreak.task_service.enums.TaskType;

public record UpdateTaskRequest(String title,
                                TaskType taskType,
                                String description,
                                Long frequencyMinutes,
                                double targetCount,
                                Long folderId) {}
