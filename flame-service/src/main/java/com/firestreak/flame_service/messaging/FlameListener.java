package com.firestreak.flame_service.messaging;

import com.firestreak.flame_service.config.RabbitMqConfig;
import com.firestreak.flame_service.service.FlameService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlameListener {
    private final FlameService flameService;

    @RabbitListener(queues = RabbitMqConfig.CREATE_WALLET_QUEUE)
    public void createWalletListener(CreateWalletEvent createWalletEvent){
        flameService.createWallet(createWalletEvent);
    }

    @RabbitListener(queues = RabbitMqConfig.DAILY_STREAK_CHECK_QUEUE)
    public void dailyStreakCheckListener(DailyStreakEvent dailyStreakEvent){
        flameService.dailyWalletUpdate(dailyStreakEvent);
    }
}
