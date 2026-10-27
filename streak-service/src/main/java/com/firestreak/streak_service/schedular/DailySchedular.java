package com.firestreak.streak_service.schedular;

import com.firestreak.streak_service.entity.UserStreak;
import com.firestreak.streak_service.messaging.DailyStreakEvent;
import com.firestreak.streak_service.repository.UserStreakRepository;
import com.firestreak.streak_service.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.firestreak.streak_service.config.RabbitMqConfiguration.DAILY_EXCHANGE;

@Component
@RequiredArgsConstructor
public class DailySchedular {
    private final RabbitTemplate template;
    private final StreakService streakService;
    private final UserStreakRepository userStreakRepository;

    @Scheduled(cron = "0 15 0 * * *")
    public void checkExpired(){
        streakService.dayEnd();
        List<UserStreak> userStreaks = userStreakRepository.findAll();
        userStreaks.forEach((el)->
                template.convertAndSend(DAILY_EXCHANGE, "", new DailyStreakEvent(el.getUserId(), el.getStreakCount())));
    }
}