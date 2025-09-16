package br.com.vitor.cybercrimes.tracker.core.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;
import java.util.function.Supplier;

@Log4j2
public abstract class IntegrationClient {

    protected <T> ResponseEntity<T> executeWithRetry(int maxRetries, Supplier<ResponseEntity<T>> supplier) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                ResponseEntity<T> responseEntity = supplier.get();

                if (responseEntity.getStatusCode().isError()) {
                    attempt++;

                    if (attempt >= maxRetries) {
                        throw new IllegalStateException("Max retries reached with error status: " + responseEntity.getStatusCode());
                    }
                }
            } catch (Exception e) {
                attempt++;
                if (attempt >= maxRetries) {
                    log.error("Max retries reached with exception: {}", e.getMessage(), e);
                    throw e;
                }
            }
        }
        throw new IllegalStateException("Should not reach here");
    }
}
