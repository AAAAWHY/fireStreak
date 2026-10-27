package com.firestreak.flame_service.messaging;

import com.firestreak.flame_service.TestcontainersConfiguration;
import com.firestreak.flame_service.config.RabbitMqConfig;
import com.firestreak.flame_service.entity.UserWallet;
import com.firestreak.flame_service.repository.UserWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RabbitMqIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserWalletRepository userWalletRepository;

    @Test
    void createWalletEvent_createsWallet() {
        long userId = 1001L;

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.CREATE_WALLET_EXCHANGE,
                RabbitMqConfig.CREATE_WALLET_KEY,
                new CreateWalletEvent(userId));

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertThat(userWalletRepository.findById(userId)).isPresent());
    }

    @Test
    void dailyStreakEvent_updatesWallet() {
        long userId = 1002L;
        userWalletRepository.save(UserWallet.builder().userId(userId).build());

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.DAILY_EXCHANGE,
                "",
                new DailyStreakEvent(userId, 5));

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    UserWallet wallet = userWalletRepository.findById(userId).orElseThrow();
                    assertThat(wallet.getCatsActive()).isTrue();
                    assertThat(wallet.getFlamesIncome()).isGreaterThan(0);
                    assertThat(wallet.getFlames()).isEqualTo(wallet.getFlamesIncome());
                });
    }
}
