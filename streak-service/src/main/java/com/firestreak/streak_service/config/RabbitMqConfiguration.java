package com.firestreak.streak_service.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {
    public static final String TASK_COMPLETED_QUEUE = "task-completed-queue";
    public static final String DAILY_EXCHANGE = "daily-exchange";


    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
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
    FanoutExchange dailyFanoutExchange(){
        return new FanoutExchange(DAILY_EXCHANGE);
    }
}