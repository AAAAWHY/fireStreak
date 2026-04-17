package com.firestreak.task_service.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Enumerated(EnumType.STRING)
public enum TaskType {
    BOOLEAN,
    QUANTITATIVE
}
