package com.firestreak.ai_service.service;

import com.firestreak.ai_service.enums.Status;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder, ChatMemory chatMemory){
        chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

//    @PostConstruct
//    public void post(){
//        System.out.println(this.askVerificationModel("1_1","я занимался 3 часа программированием прошел что такое инт"));
//    }

    public record Verdict(
            Status status,
            String responseMessage
    ) {}

    public Verdict askVerificationModel(String user_taskId, String message, String taskTitle, String taskDescription, Double targetCount){
        return chatClient.prompt()
                .system("Ты проверяешь правда ли человек выполнил задание по описанию того что он сделал или врет. " +
                        "вот задание которое выполнял пользователь: " +
                        "назваине: "+taskTitle +
                        (taskDescription == null ? "" : (" описание: "+taskDescription)) +
                        (targetCount == null ? "" : (" необходимое количетво выпаолнений: " + targetCount))+
                        " возвращай сообщение в четко заданном JSON виде с правильным типом данных " +
                        "отвечай на том же языке что и пользователь. если не уверен в честонсти то задай доп вопрос но не делай сликшом длинного вопроса делай его чтобы на него было не лень отвечать")
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, user_taskId))
                .call()
                .entity(Verdict.class);
    }
}