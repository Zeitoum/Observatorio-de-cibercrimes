package br.com.vitor.cybercrimes.tracker.core.legalprocess.service;

import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessFileLine;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.LegalProcessFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileProcessorService {

    private final LegalProcessFileService legalProcessFileService;
    private final LegalProcessService legalProcessService;

    public void processSpreadsheetFile(String spreadsheetFilePath) {
        log.info("Extracting file information from spreadsheet: {}", spreadsheetFilePath);

        Path filePath = Path.of(spreadsheetFilePath);
        // Get the number of available processors to determine the thread pool size
        final int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line ->
                executor.submit(() -> {
                    try {
                        processFileLine(line);
                    } catch (Exception e) {
                        throw new RuntimeException("Error processing line: " + line, e);
                    }
                }));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + spreadsheetFilePath, e);
        } finally {
            // Ensure the executor is properly shut down (this will wait for all tasks to complete)
            executor.shutdown();
        }
    }

    private void processFileLine(String line) {
        LegalProcessFileLine fileLine = new LegalProcessFileLine(line, ";");

        if (fileLine.isHeader()) {
            return;
        } else if (legalProcessService.exists(fileLine)) {
            log.info("Legal process already exists: {}", fileLine.getAcordaoCode());
            return;
        }

        createLegalProcess(fileLine);
    }

    private void createLegalProcess(LegalProcessFileLine fileLine) {
        legalProcessService.create(fileLine, null);
    }
}
