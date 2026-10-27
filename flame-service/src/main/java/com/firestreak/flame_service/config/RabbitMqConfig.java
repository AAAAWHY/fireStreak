package com.firestreak.flame_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String DAILY_STREAK_CHECK_QUEUE = "daily-streak-check-queue";
    public static final String CREATE_WALLET_QUEUE = "create-wallet-queue";
    public static final String DAILY_EXCHANGE = "daily-exchange";
    public final static String CREATE_WALLET_KEY = "create-wallet-key";
    public final static String CREATE_WALLET_EXCHANGE = "create-wallet-exchange";


    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    Queue dailyQueue(){return new Queue(DAILY_STREAK_CHECK_QUEUE);}

    @Bean
    FanoutExchange dailyFanoutExchange(){
        return new FanoutExchange(DAILY_EXCHANGE);
    }

    @Bean
    Binding dailyBinding(){
        return BindingBuilder
                .bind(dailyQueue())
                .to(dailyFanoutExchange());
    }

//    @Bean
//    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//            ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter());
//        return factory;
//    }

    @Bean
    Queue createWalletQueue(){return new Queue(CREATE_WALLET_QUEUE);}

    @Bean
    DirectExchange createWalletExchange() {return new DirectExchange(CREATE_WALLET_EXCHANGE);}

    @Bean
    Binding createWalletBinging(){
        return BindingBuilder
                .bind(createWalletQueue())
                .to(createWalletExchange())
                .with(CREATE_WALLET_KEY);
    }
}