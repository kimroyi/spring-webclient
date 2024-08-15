package com.royi.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {
    private String baseUrl;

    // Getter
    public String getBaseUrl() {
        return baseUrl;
    }

    // Setter
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}