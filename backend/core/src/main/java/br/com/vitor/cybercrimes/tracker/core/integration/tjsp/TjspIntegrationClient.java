package br.com.vitor.cybercrimes.tracker.core.integration.tjsp;

import br.com.vitor.cybercrimes.tracker.core.integration.IntegrationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TjspIntegrationClient extends IntegrationClient {

    private final RestTemplate restTemplate;

    public byte[] getPdfFile(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> responseEntity = executeWithRetry(
                3, () -> restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class));

        return responseEntity.getBody();
    }
}
