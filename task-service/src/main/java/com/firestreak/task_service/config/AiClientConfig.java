package com.firestreak.task_service.config;

import com.firestreak.task_service.dto.AskModelDto;
import com.firestreak.task_service.dto.VerdictDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AiClientConfig {
    @Bean
    AiClient aiClient(){
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8085")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(AiClient.class);
    }
}