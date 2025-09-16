package br.com.vitor.cybercrimes.tracker.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJdbcRepositories(basePackages = "br.com.vitor.cybercrimes.tracker.core.legalprocess.repository")
@SpringBootApplication(scanBasePackages = "br.com.vitor.cybercrimes.tracker")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
