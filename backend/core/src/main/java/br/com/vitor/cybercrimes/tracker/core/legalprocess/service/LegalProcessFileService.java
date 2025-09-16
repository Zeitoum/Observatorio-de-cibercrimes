package br.com.vitor.cybercrimes.tracker.core.legalprocess.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import br.com.vitor.cybercrimes.tracker.core.integration.tjsp.TjspIntegrationClient;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.repository.LegalProcessFileRepository;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.LegalProcessFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static br.com.vitor.cybercrimes.tracker.core.utils.FileUtils.fileIsValid;
import static br.com.vitor.cybercrimes.tracker.core.utils.FileUtils.getFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class LegalProcessFileService {

    private final LegalProcessFileRepository repository;
    private final TjspIntegrationClient tjspIntegrationClient;

    @Value("${legalprocesses.files.path}")
    private String filesDirectory;

    public LegalProcessFile create(String fileUrl, String fileName) {
        final Path filePath = Path.of(filesDirectory, fileName);

        File file = downloadFileAndSaveInDisk(fileUrl, filePath);

        LegalProcessFile legalProcessFile = buildLegalProcessFile(file);

        return repository.save(legalProcessFile);
    }

    public File downloadFileAndSaveInDisk(String fileUrl, Path targetPath) {
        log.info("Saving file in disk from URL: {}", fileUrl);

        try (InputStream inputStream = new URL(fileUrl).openStream()) {

            Files.createDirectories(targetPath.getParent());
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File downloaded and saved in disk successfully to: {}", targetPath.toAbsolutePath());

            return getFile(targetPath);
        } catch (IOException e) {
            log.error("Failed to download or save file from URL: {}", fileUrl, e);
            throw new RuntimeException("Error saving file from URL: " + fileUrl, e);
        }
    }

    private LegalProcessFile buildLegalProcessFile(File file) {
        LegalProcessFile legalProcessFile = new LegalProcessFile();
        legalProcessFile.setFileName(file.getName());
        legalProcessFile.setFilePath(file.getAbsolutePath());
        legalProcessFile.setFileSize(file.length());

        return legalProcessFile;
    }
}
