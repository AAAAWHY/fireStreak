package com.firestreak.notification_service.controller;

import com.firestreak.notification_service.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/push")
@RequiredArgsConstructor
public class NotificationController {

    private final PushService pushService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestParam String token,
                                       @RequestParam String title,
                                       @RequestParam String body) {
        String messageId = pushService.sendToToken(token, title, body);
        return ResponseEntity.ok("Отправлено: " + messageId);
    }
}