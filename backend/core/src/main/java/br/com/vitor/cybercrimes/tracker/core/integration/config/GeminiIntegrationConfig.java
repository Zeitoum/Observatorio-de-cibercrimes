package br.com.vitor.cybercrimes.tracker.core.integration.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiIntegrationConfig {

    private final String geminiApiKey;

    public GeminiIntegrationConfig(@Value("${gemini.api.key}") String geminiApiKey) {
        this.geminiApiKey = geminiApiKey;
    }

    @Bean("geminiAiClient")
    public Client geminiAiClient() {
        return Client.builder()
                .apiKey(geminiApiKey)
                .build();
    }
}
