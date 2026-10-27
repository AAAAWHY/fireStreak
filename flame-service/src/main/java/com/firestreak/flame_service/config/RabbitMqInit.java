package com.firestreak.flame_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqInit {
    private final ConnectionFactory connectionFactory;
    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        connectionFactory.createConnection().close();
    }
}