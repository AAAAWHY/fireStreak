package com.firestreak.streak_service.schedular;

import com.firestreak.streak_service.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSchedular {
    private final StreakService streakService;

    @Scheduled(cron = "0 15 0 * * *")
    public void checkExpired(){
        streakService.everyDayCheck();
    }
}
