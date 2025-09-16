package br.com.vitor.cybercrimes.tracker.core.legalprocess.service;

import br.com.vitor.cybercrimes.tracker.core.integration.geminiai.GeminiAiIntegrationClient;
import br.com.vitor.cybercrimes.tracker.core.legalprocess.dto.LegalProcessPdfInfo;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel1;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel2;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.LegalProcessFile;
import com.google.genai.errors.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static br.com.vitor.cybercrimes.tracker.core.utils.FileUtils.fileIsValid;
import static br.com.vitor.cybercrimes.tracker.core.utils.FileUtils.getFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class LegalProcessClassifierService {

    private static final String POSITIVE = "Sim";

    private final LegalProcessService legalProcessService;
    private final LegalProcessFileService legalProcessFileService;
    private final GeminiAiIntegrationClient geminiAiIntegrationClient;

    public void classifyLegalProcesses() throws InterruptedException {
        List<LegalProcessPdfInfo> allPdfInfo = legalProcessService.findAllNotClassified(1, 100);

        for (LegalProcessPdfInfo pdfInfo : allPdfInfo) {
            Thread.sleep(6000); // O modelo Gemini 2.5 Flash permite apenas 10 requisições por minuto
            try {
                downloadProcessPdf(pdfInfo);
            } catch (Exception e) {
                log.error("Error downloading PDF", e);
                continue;
            }
            try {
                classifyUsingAi(pdfInfo);
            } catch (ClientException e) {
                log.error("The PDF file is corrupted ! Continuing with others ...", e);
            }
        }
    }

    private void downloadProcessPdf(LegalProcessPdfInfo legalProcessDTO) {
        if (legalProcessDTO.getFilePath() != null && fileIsValid(Path.of(legalProcessDTO.getFilePath())))
            return;

        LegalProcessFile legalProcessFile = legalProcessFileService.create(
                legalProcessDTO.getPdfUrl(), legalProcessDTO.getAcordaoCode() + ".pdf");

        legalProcessService.updateFileId(legalProcessDTO.getLegalProcessId(), legalProcessFile.getId());

        legalProcessDTO.setFilePath(legalProcessFile.getFilePath());
    }

    private void classifyUsingAi(LegalProcessPdfInfo legalProcessDTO) {
        if (legalProcessDTO.getFilePath() == null) return;

        File file = getFile(Path.of(legalProcessDTO.getFilePath()));

        String result = geminiAiIntegrationClient.classifyFileWithAi(file);

        processClassificationResult(legalProcessDTO, result);
    }

    private void processClassificationResult(LegalProcessPdfInfo legalProcessDTO, String result) {
        if (result == null || result.isBlank()) {
            log.warn("Classification result is empty for legal process: {}", legalProcessDTO.getLegalProcessId());
            return;
        }

        try {
            String[] parts = result.split("\n");
            String status = parts[0].split(":")[1].replaceAll("\\*|\\s", "");

            boolean isCybercrime = status.equals(POSITIVE);
            ClassificationLevel2 classificationLevel2 = null;
            ClassificationLevel1 classificationLevel1 = null;
            String justification = null;

            if (isCybercrime) {
                String classification = parts[1].split(":")[1].replaceAll("\\*|\\s", "");
                justification = parts[2].split(":")[1].replaceAll("\\*", "").trim();
                classificationLevel2 = ClassificationLevel2.valueOf(classification.toUpperCase());
                classificationLevel1 = classificationLevel2.getClassificationLevel1();
            }

            legalProcessService.updateClassification(legalProcessDTO.getLegalProcessId(), isCybercrime, classificationLevel1,
                    classificationLevel2, justification);

            log.info("Legal process acordaoCode {} classified as Cybercrime: {}, classification: {}",
                    legalProcessDTO.getAcordaoCode(), isCybercrime, classificationLevel2);
        } catch (Exception e) {
            log.error("Error while trying to classify the legal process. Result from IA: " + result, e);
        }
    }
}
