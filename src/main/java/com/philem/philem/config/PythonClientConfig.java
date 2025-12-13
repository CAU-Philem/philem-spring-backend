package com.philem.philem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PythonClientConfig {

    @Bean
    public WebClient pythonWebClient(
            @Value("${philem.python-base-url}") String pythonBaseUrl
    ) {
        return WebClient.builder()
                .baseUrl(pythonBaseUrl)
                .build();
    }
}
