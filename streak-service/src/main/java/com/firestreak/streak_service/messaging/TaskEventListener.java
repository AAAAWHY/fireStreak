package com.firestreak.streak_service.messaging;

import com.firestreak.streak_service.config.RabbitMqConfiguration;
import com.firestreak.streak_service.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskEventListener {
    private final StreakService streakService;

    @RabbitListener(queues = RabbitMqConfiguration.TASK_COMPLETED_QUEUE)
    public void handleTaskCompletedEvent(TaskCompletedEvent taskCompletedEvent){
        streakService.handleCompletedTask(taskCompletedEvent);
    }
}