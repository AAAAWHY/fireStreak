package com.firestreak.ai_service.controller;

import com.firestreak.ai_service.AskModelDto;
import com.firestreak.ai_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ai-service")
@RequiredArgsConstructor
public class Controller {
    private final ChatService chatService;

    @PostMapping("/vedrict")
    ChatService.Verdict getVerdict(@RequestBody AskModelDto askModelDto){
        return chatService.askVerificationModel(""+askModelDto.userId()+"_"+askModelDto.taskId(),
                askModelDto.message(),
                askModelDto.taskTitle(),
                askModelDto.taskDescription(),
                askModelDto.targetCount());
    }
}
