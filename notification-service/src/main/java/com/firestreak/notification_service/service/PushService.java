package com.firestreak.notification_service.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushService {

    private final FirebaseMessaging firebaseMessaging;

    public String sendToToken(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .build();

        try {
            String messageId = firebaseMessaging.send(message);
            System.out.println("Успешно отправлено, id: " + messageId);
            return messageId;
        } catch (FirebaseMessagingException e) {
            System.out.println("Ошибка отправки: " + e.getMessagingErrorCode());
            throw new RuntimeException("Не удалось отправить push", e);
        }
    }
}