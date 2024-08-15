package com.royi.api.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Autowired
    private WebClientProperties properties;

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000) // 연결타임아웃 50초
            .responseTimeout(Duration.ofMillis(50000)); // 응답타임아웃 50초

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Accept", "application/json")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}