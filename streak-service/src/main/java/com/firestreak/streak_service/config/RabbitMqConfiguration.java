package com.firestreak.streak_service.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {
    public static final String TASK_COMPLETED_QUEUE = "task-completed-queue";
}
