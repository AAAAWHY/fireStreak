package com.firestreak.task_service.config;

import com.firestreak.task_service.dto.AskModelDto;
import com.firestreak.task_service.dto.VerdictDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface AiClient {
    @PostExchange("api/ai-service/vedrict")
    VerdictDto aiVerdict(@RequestBody AskModelDto askModelDto);
}
