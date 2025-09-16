package br.com.vitor.cybercrimes.tracker.core.legalprocess.scheduler;

import br.com.vitor.cybercrimes.tracker.core.legalprocess.service.LegalProcessClassifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class LegalProcessClassifierScheduler {

    private final LegalProcessClassifierService service;

    @Scheduled(fixedDelay = 1000) // 1 second
    public void execute() throws InterruptedException {
        log.info("Starting Legal Process Classifier Scheduler...");

        service.classifyLegalProcesses();

        log.info("Legal Process classification finished !");
    }
}
