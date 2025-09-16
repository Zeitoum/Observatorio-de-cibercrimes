package br.com.vitor.cybercrimes.tracker.core.legalprocess.dto;

import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel1;
import br.com.vitor.cybercrimes.tracker.model.legalprocess.ClassificationLevel2;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LegalProcessDTO {

    private Long id;
    private String processCode;
    private Long acordaoCode;
    private String className;
    private String subject;
    private String rapporteur;
    private String district;
    private String judgingBody;
    private LocalDate judgmentDate;
    private LocalDate publishDate;
    private String headnote;
    private Long legalProcessFileId;
    private String pdfUrl;
    private Boolean isCybercrime;
    private ClassificationLevel1 classificationLevel1;
    private ClassificationLevel2 classificationLevel2;
    private String justification;
}
