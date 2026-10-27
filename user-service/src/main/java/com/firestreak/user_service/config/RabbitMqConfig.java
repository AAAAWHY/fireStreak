package com.firestreak.user_service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public final static String CREATE_WALLET_EXCHANGE = "create-wallet-exchange";
    public final static String CREATE_WALLET_KEY = "create-wallet-key";

    @Bean
    MessageConverter messageConverter(){return new JacksonJsonMessageConverter();}

    @Bean
    DirectExchange createWalletExchange(){
        return new DirectExchange(CREATE_WALLET_EXCHANGE);
    }
}