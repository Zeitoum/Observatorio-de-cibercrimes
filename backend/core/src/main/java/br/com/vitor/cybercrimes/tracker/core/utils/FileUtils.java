package br.com.vitor.cybercrimes.tracker.core.utils;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FileUtils {

    public static byte[] getBytesFrom(String filePath) {
        return getBytesFrom(new File(filePath));
    }

    public static byte[] getBytesFrom(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("Error reading file: " + file.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }

    public static File getFile(Path filePath) {
        return new File(filePath.toAbsolutePath().toString());
    }

    public static boolean fileIsValid(Path filePath) {
        try {
            if (!Files.isReadable(filePath)) {
                log.warn("File is not readable: {}", filePath);
                return false;
            }

            long fileSize = Files.size(filePath);
            if (fileSize <= 0) {
                log.warn("File is empty or invalid size: {}", filePath);
                return false;
            }

            // Tenta abrir o arquivo para verificar se não está corrompido
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    // Verifica se há erros durante a leitura
                }
                return true;
            }
        } catch (IOException e) {
            log.error("Error validating file: {}", filePath, e);
            return false;
        }
    }
}
