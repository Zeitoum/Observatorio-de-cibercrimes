package br.com.vitor.cybercrimes.tracker.core.legalprocess.dto;

import lombok.Data;

@Data
public class LegalProcessPdfInfo {

    private Long legalProcessId;
    private Long acordaoCode;
    private String pdfUrl;
    private String filePath;
}
