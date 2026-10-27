package com.firestreak.task_service.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class RabbitMqConfig {
    public static String TASK_EXCHANGE = "taskExchange";
    public static final String TASK_COMPLETED_QUEUE = "task-completed-queue";
    public static final String TASK_COMPLETED_ROUTING_KEY = "task.completed.key";

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(TASK_EXCHANGE);
    }

    @Bean
    Queue taskCompletedQueue(){
        return new Queue(TASK_COMPLETED_QUEUE);
    }

    @Bean
    Binding taskCompletedBinding(){
        return BindingBuilder.bind(taskCompletedQueue()).to(exchange()).with(TASK_COMPLETED_ROUTING_KEY);
    }

    @Bean
    MessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}